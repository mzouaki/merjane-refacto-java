package com.nimbleways.springboilerplate.service;

import com.nimbleways.springboilerplate.domain.Product;
import com.nimbleways.springboilerplate.service.handler.ExpirableProductHandler;
import com.nimbleways.springboilerplate.service.handler.NormalProductHandler;
import com.nimbleways.springboilerplate.service.handler.SeasonalProductHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {
    private final NormalProductHandler normalProductHandler;
    private final SeasonalProductHandler seasonalProductHandler;
    private final ExpirableProductHandler expirableProductHandler;

    public void processProducts(Set<Product> products) {
        if (products == null || products.isEmpty()) {
            log.warn("Order without product is sent, abort");
            return;
        }
        products.forEach(product -> {
            log.info("Processing product {} of type {}", product.getName(), product.getType());
            switch (product.getType()) {
                case NORMAL -> normalProductHandler.handle(product);
                case SEASONAL -> seasonalProductHandler.handle(product);
                case EXPIRABLE -> expirableProductHandler.handle(product);
            }
        });
    }
}