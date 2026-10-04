package com.nimbleways.springboilerplate.service.handler;

import com.nimbleways.springboilerplate.domain.Product;
import com.nimbleways.springboilerplate.service.NotificationService;
import com.nimbleways.springboilerplate.service.manager.ProductInventoryManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import static com.nimbleways.springboilerplate.service.validator.ProductAvailabilityValidator.arrivesAfterSeasonEnd;
import static com.nimbleways.springboilerplate.service.validator.ProductAvailabilityValidator.isAvailable;
import static com.nimbleways.springboilerplate.service.validator.ProductAvailabilityValidator.isInSeason;
import static com.nimbleways.springboilerplate.service.validator.ProductAvailabilityValidator.notInSeason;


@Service
@Slf4j
@RequiredArgsConstructor
public class SeasonalProductHandler implements ProductHandler {
    private final ProductInventoryManager productInventoryManager;
    private final NotificationService notificationService;

    @Override
    public void handle(Product product) {
        final LocalDate today = LocalDate.now();
        if (isAvailable(product) && isInSeason(product, today)) {
            log.debug("Seasonal product {} is available in season, processing it", product.getName());
            productInventoryManager.decrementStock(product);
            return;
        }
        if (arrivesAfterSeasonEnd(product, today)) {
            log.debug("Seasonal product {} arrives after season end, notifying out of stock", product.getName());
            productInventoryManager.markOutOfStock(product);
            notificationService.sendOutOfStockNotification(product.getName());
            return;
        }
        if (notInSeason(product, today)) {
            log.debug("Seasonal product {} is not in season, notifying out of stock", product.getName());
            notificationService.sendOutOfStockNotification(product.getName());
            return;
        }
        log.debug("Seasonal product {} is delayed, notifying delay", product.getName());
        notificationService.sendDelayNotification(product.getLeadTime(), product.getName());
    }
}
