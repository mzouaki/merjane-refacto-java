package com.nimbleways.springboilerplate.service;

import com.nimbleways.springboilerplate.domain.Product;
import com.nimbleways.springboilerplate.service.handler.ExpirableProductHandler;
import com.nimbleways.springboilerplate.service.handler.NormalProductHandler;
import com.nimbleways.springboilerplate.service.handler.SeasonalProductHandler;
import com.nimbleways.springboilerplate.utils.Annotations.UnitTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Set;

import static com.nimbleways.springboilerplate.domain.ProductType.EXPIRABLE;
import static com.nimbleways.springboilerplate.domain.ProductType.NORMAL;
import static com.nimbleways.springboilerplate.domain.ProductType.SEASONAL;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@UnitTest
@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private NormalProductHandler normalProductHandler;

    @Mock
    private SeasonalProductHandler seasonalProductHandler;

    @Mock
    private ExpirableProductHandler expirableProductHandler;

    @InjectMocks
    private ProductService productService;

    @Test
    public void shouldDelegateToNormalHandler() {
        Product product = new Product(1L, 15, 3, NORMAL, "RJ45 Cable", null, null, null);
        productService.processProducts(Set.of(product));

        verify(normalProductHandler).handle(product);
        verifyNoInteractions(seasonalProductHandler, expirableProductHandler);
    }

    @Test
    public void shouldDelegateToSeasonalHandler() {
        LocalDate today = LocalDate.now();
        Product product = new Product(1L, 15, 3, SEASONAL, "Watermelon", null, today.minusDays(1), today.plusDays(1));
        productService.processProducts(Set.of(product));

        verify(seasonalProductHandler).handle(product);
        verifyNoInteractions(normalProductHandler, expirableProductHandler);
    }

    @Test
    public void shouldDelegateToExpirableHandler() {
        LocalDate today = LocalDate.now();
        Product product = new Product(1L, 15, 3, EXPIRABLE, "Butter", today.plusDays(10), null, null);
        productService.processProducts(Set.of(product));

        verify(expirableProductHandler).handle(product);
        verifyNoInteractions(normalProductHandler, seasonalProductHandler);
    }
}
