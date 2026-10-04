package com.nimbleways.springboilerplate.service.handler;

import com.nimbleways.springboilerplate.domain.Product;
import com.nimbleways.springboilerplate.service.NotificationService;
import com.nimbleways.springboilerplate.service.manager.ProductInventoryManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import static com.nimbleways.springboilerplate.service.validator.ProductAvailabilityValidator.hasNotExpired;
import static com.nimbleways.springboilerplate.service.validator.ProductAvailabilityValidator.isAvailable;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExpirableProductHandler implements ProductHandler {
    private final ProductInventoryManager productInventoryManager;
    private final NotificationService notificationService;

    @Override
    public void handle(Product product) {
        final LocalDate today = LocalDate.now();
        if (isAvailable(product) && hasNotExpired(product, today)) {
            log.debug("Expirable product {} is and has not expired, processing it", product.getName());
            productInventoryManager.decrementStock(product);
        } else {
            log.debug("Expirable product {} has expired, notifying expiration", product.getName());
            productInventoryManager.markOutOfStock(product);
            notificationService.sendDelayNotification(product.getLeadTime(), product.getName());
        }
    }
}
