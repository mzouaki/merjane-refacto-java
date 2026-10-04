package com.nimbleways.springboilerplate.service.handler;

import com.nimbleways.springboilerplate.domain.Product;
import com.nimbleways.springboilerplate.service.NotificationService;
import com.nimbleways.springboilerplate.service.manager.ProductInventoryManager;
import com.nimbleways.springboilerplate.utils.Annotations.UnitTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static com.nimbleways.springboilerplate.domain.ProductType.SEASONAL;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(SpringExtension.class)
@UnitTest
public class SeasonalProductHanderTest {

    @Mock
    private ProductInventoryManager productInventoryManager;
    @Mock
    private NotificationService notificationService;
    @InjectMocks
    private SeasonalProductHandler seasonalProductHandler;

    @Test
    public void shouldDecrementStock() {
        LocalDate today = LocalDate.now();
        Product product = new Product(1L, 15, 3, SEASONAL, "Watermelon", null, today.minusDays(1), today.plusDays(1));

        seasonalProductHandler.handle(product);

        verify(productInventoryManager).decrementStock(product);
        verifyNoInteractions(notificationService);
    }

    @Test
    public void shouldNotifyOutOfStockAndMarkProductOutOfStockWhenArrivesAfterSeasonEnd() {
        LocalDate today = LocalDate.now();
        Product product = new Product(1L, 15, 3, SEASONAL, "Watermelon", null, today.plusDays(1), today.plusDays(1));

        seasonalProductHandler.handle(product);

        verify(notificationService).sendOutOfStockNotification("Watermelon");
        verify(productInventoryManager).markOutOfStock(product);
    }

    @Test
    public void shouldNotifyOutOfStockOnlyWhenSeasonHasNotStartedYet() {
        LocalDate today = LocalDate.now();
        Product product = new Product(1L, 5, 3, SEASONAL, "Watermelon", null, today.plusDays(2), today.plusDays(30));

        seasonalProductHandler.handle(product);

        verify(notificationService).sendOutOfStockNotification("Watermelon");
        verifyNoInteractions(productInventoryManager);
    }

    @Test
    public void shouldNotifyDelayWhenSeasonalProductIsDelayed() {
        LocalDate today = LocalDate.now();
        Product product = new Product(1L, 5, 0, SEASONAL, "Watermelon", null, today.minusDays(2), today.plusDays(30));

        seasonalProductHandler.handle(product);

        verify(notificationService).sendDelayNotification(5, "Watermelon");
        verifyNoInteractions(productInventoryManager);
    }


}