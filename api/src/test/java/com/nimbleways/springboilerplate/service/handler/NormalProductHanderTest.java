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

import static com.nimbleways.springboilerplate.domain.ProductType.NORMAL;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(SpringExtension.class)
@UnitTest
public class NormalProductHanderTest {

    @Mock
    private ProductInventoryManager productInventoryManager;
    @Mock
    private NotificationService notificationService;
    @InjectMocks
    private NormalProductHandler normalProductHandler;

    @Test
    public void shouldDecrementStock() {

        Product product = new Product(1L, 15, 3, NORMAL, "RJ45 Cable", null, null, null);

        normalProductHandler.handle(product);

        verify(productInventoryManager).decrementStock(product);
        verifyNoInteractions(notificationService);
    }

    @Test
    public void shouldNotifyDelay() {

        Product product = new Product(1L, 15, 0, NORMAL, "RJ45 Cable", null, null, null);

        normalProductHandler.handle(product);

        verify(notificationService).sendDelayNotification(15, "RJ45 Cable");
        verifyNoInteractions(productInventoryManager);
    }
}