package com.nimbleways.springboilerplate.service.handler;

import com.nimbleways.springboilerplate.domain.Product;
import com.nimbleways.springboilerplate.service.NotificationService;
import com.nimbleways.springboilerplate.service.manager.ProductInventoryManager;
import com.nimbleways.springboilerplate.service.validator.ProductAvailabilityValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NormalProductHandler implements ProductHandler {
    private final ProductInventoryManager productInventoryManager;
    private final NotificationService notificationService;

    @Override
    public void handle(Product product) {
        if (ProductAvailabilityValidator.isAvailable(product)) {
            log.debug("Normal product {} is available, processing it", product.getName());
            productInventoryManager.decrementStock(product);
        } else {
            log.debug("Normal product {} is not available, notifying delay", product.getName());
            notificationService.sendDelayNotification(product.getLeadTime(), product.getName());
        }
    }
}
