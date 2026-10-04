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

import static com.nimbleways.springboilerplate.domain.ProductType.EXPIRABLE;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(SpringExtension.class)
@UnitTest
public class ExpirableProductHanderTest {

    @Mock
    private ProductInventoryManager productInventoryManager;
    @Mock
    private NotificationService notificationService;
    @InjectMocks
    private ExpirableProductHandler expirableProductHandler;

    @Test
    public void shouldDecrementStock() {
        LocalDate today = LocalDate.now();
        Product product = new Product(1L, 15, 3, EXPIRABLE, "Butter", today.plusDays(10), null, null);

        expirableProductHandler.handle(product);

        verify(productInventoryManager).decrementStock(product);
        verifyNoInteractions(notificationService);
    }

    @Test
    public void shouldNotifyExpirationAndMarkProductOutOfStockWhenProductHasExpired() {
        LocalDate today = LocalDate.now();
        Product product = new Product(1L, 15, 3, EXPIRABLE, "Milk", today.minusDays(1), null, null);

        expirableProductHandler.handle(product);

        verify(notificationService).sendExpirationNotification("Milk", today.minusDays(1));
        verify(productInventoryManager).markOutOfStock(product);
    }

    @Test
    public void shouldNotifyExpirationAndMarkProductOutOfStockWhenProductIsUnavailable() {
        LocalDate today = LocalDate.now();
        Product product = new Product(1L, 15, 0, EXPIRABLE, "Milk", today.plusDays(1), null, null);

        expirableProductHandler.handle(product);

        verify(notificationService).sendExpirationNotification("Milk", today.plusDays(1));
        verify(productInventoryManager).markOutOfStock(product);
    }


}