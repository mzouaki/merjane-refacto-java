package com.nimbleways.springboilerplate.service.handler;

import com.nimbleways.springboilerplate.domain.Product;
import com.nimbleways.springboilerplate.service.NotificationService;
import com.nimbleways.springboilerplate.service.manager.ProductInventoryManager;
import com.nimbleways.springboilerplate.service.validator.ProductAvailabilityValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;


@Service
@Slf4j
@RequiredArgsConstructor
public class SeasonalProductHandler implements ProductHandler {
    private final ProductInventoryManager productInventoryManager;
    private final NotificationService notificationService;

    @Override
    public void handle(Product product) {
        final LocalDate today = LocalDate.now();
        if (ProductAvailabilityValidator.isAvailable(product) && ProductAvailabilityValidator.isInSeason(product, today)) {
            log.debug("Seasonal product {} is available in season, processing it", product.getName());
            productInventoryManager.decrementStock(product);
            return;
        }
        if (ProductAvailabilityValidator.arrivesAfterSeasonEnd(product, today)) {
            log.debug("Seasonal product {} arrives after season end, notifying out of stock", product.getName());
            notificationService.sendOutOfStockNotification(product.getName());
            productInventoryManager.markOutOfStock(product);
            return;
        }
        if (ProductAvailabilityValidator.notInSeason(product, today)) {
            log.debug("Seasonal product {} is not in season, notifying out of stock", product.getName());
            notificationService.sendOutOfStockNotification(product.getName());
            return;
        }
        log.debug("Seasonal product {} is delayed, notifying delay", product.getName());
        notificationService.sendDelayNotification(product.getLeadTime(), product.getName());
    }
}
