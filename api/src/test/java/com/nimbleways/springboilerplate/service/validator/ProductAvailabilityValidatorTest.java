package com.nimbleways.springboilerplate.service.validator;

import com.nimbleways.springboilerplate.domain.Product;
import com.nimbleways.springboilerplate.utils.Annotations.UnitTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static com.nimbleways.springboilerplate.domain.ProductType.EXPIRABLE;
import static com.nimbleways.springboilerplate.domain.ProductType.NORMAL;
import static com.nimbleways.springboilerplate.domain.ProductType.SEASONAL;

@ExtendWith(SpringExtension.class)
@UnitTest
public class ProductAvailabilityValidatorTest {

    @Test
    public void shouldReturnTrueProductAvailable() {
        Product product = new Product(1L, 15, 3, NORMAL, "RJ45 Cable", null, null, null);

        Assertions.assertThat(ProductAvailabilityValidator.isAvailable(product)).isTrue();
    }

    @Test
    public void shouldReturnFalseProductNotAvailable() {
        Product product = new Product(1L, 15, 0, NORMAL, "RJ45 Cable", null, null, null);

        Assertions.assertThat(ProductAvailabilityValidator.isAvailable(product)).isFalse();
    }

    @Test
    public void shouldReturnTrueTodayMatchesSeason() {
        LocalDate today = LocalDate.now();
        Product product = new Product(1L, 15, 3, SEASONAL, "Watermelon", null, today.minusDays(1), today.plusDays(1));

        Assertions.assertThat(ProductAvailabilityValidator.isInSeason(product, today)).isTrue();
    }

    @Test
    public void shouldReturnFalseTodayBeforeStartDay() {
        LocalDate today = LocalDate.now();
        Product product = new Product(1L, 15, 3, SEASONAL, "Watermelon", null, today.plusDays(1), today.plusDays(3));

        Assertions.assertThat(ProductAvailabilityValidator.isInSeason(product, today)).isFalse();
    }

    @Test
    public void shouldReturnFalseTodayAfterEndDay() {
        LocalDate today = LocalDate.now();
        Product product = new Product(1L, 15, 3, SEASONAL, "Watermelon", null, today.minusDays(2), today.minusDays(1));

        Assertions.assertThat(ProductAvailabilityValidator.isInSeason(product, today)).isFalse();
    }

    @Test
    public void shouldReturnFalseProductHasNotExpired() {
        LocalDate today = LocalDate.now();
        Product product = new Product(1L, 15, 3, EXPIRABLE, "Butter", today.plusDays(10), null, null);

        Assertions.assertThat(ProductAvailabilityValidator.hasNotExpired(product, today)).isTrue();
    }

    @Test
    public void shouldReturnFalseProductHasExpired() {
        LocalDate today = LocalDate.now();
        Product product = new Product(1L, 15, 3, EXPIRABLE, "Butter", today.minusDays(10), null, null);

        Assertions.assertThat(ProductAvailabilityValidator.hasNotExpired(product, today)).isFalse();
    }

    @Test
    public void shouldReturnTrueWhenDeliveryArrivesAfterSeason() {
        LocalDate today = LocalDate.now();
        Product product = new Product(1L, 10, 3, SEASONAL, "Watermelon", null, today.plusDays(1), today.plusDays(1));

        Assertions.assertThat(ProductAvailabilityValidator.arrivesAfterSeasonEnd(product, today)).isTrue();
    }

    @Test
    public void shouldReturnFalseWhenDeliveryArrivesBeforeSeason() {
        LocalDate today = LocalDate.now();
        Product product = new Product(1L, 2, 3, SEASONAL, "Watermelon", null, today.plusDays(1), today.plusDays(10));

        Assertions.assertThat(ProductAvailabilityValidator.arrivesAfterSeasonEnd(product, today)).isFalse();
    }

    @Test
    public void shouldReturnTrueWhenSeasonHasNotStarted() {
        LocalDate today = LocalDate.now();
        Product product = new Product(1L, 5, 3, SEASONAL, "Watermelon", null, today.plusDays(1), today.plusDays(10));

        Assertions.assertThat(ProductAvailabilityValidator.notInSeason(product, today)).isTrue();
    }

    @Test
    public void shouldReturnFalseWhenSeasonHasStarted() {
        LocalDate today = LocalDate.now();
        Product product = new Product(1L, 5, 3, SEASONAL, "Watermelon", null, today.minusDays(1), today.plusDays(10));

        Assertions.assertThat(ProductAvailabilityValidator.notInSeason(product, today)).isFalse();
    }
}