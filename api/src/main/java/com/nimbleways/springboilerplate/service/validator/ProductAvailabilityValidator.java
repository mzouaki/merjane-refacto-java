package com.nimbleways.springboilerplate.service.validator;

import com.nimbleways.springboilerplate.domain.Product;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;

// Utility class for validating product on different criteria stock, season and expiry date
@UtilityClass
public class ProductAvailabilityValidator {
    public boolean isAvailable(Product product) {
        return product.getAvailableStock() > 0;
    }

    public boolean isInSeason(Product product, LocalDate today) {
        return today.isAfter(product.getSeasonStartDate()) && today.isBefore(product.getSeasonEndDate());
    }

    public boolean hasNotExpired(Product product, LocalDate today) {
        return product.getExpiryDate().isAfter(today);
    }

    public boolean arrivesAfterSeasonEnd(Product product, LocalDate today) {
        return today.plusDays(product.getLeadTime()).isAfter(product.getSeasonEndDate());
    }

    public boolean notInSeason(Product product, LocalDate today) {
        return product.getSeasonStartDate().isAfter(today);
    }
}
