package com.nimbleways.springboilerplate.service.manager;

import com.nimbleways.springboilerplate.domain.Product;
import com.nimbleways.springboilerplate.mapper.ProductMapper;
import com.nimbleways.springboilerplate.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

// this class is responsible for managing product inventory, including decrementing stock and marking product out of stock.
@Service
@RequiredArgsConstructor
@Slf4j
public class ProductInventoryManager {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public void decrementStock(Product product) {
        int availableStock = product.getAvailableStock();
        // double check available stock to not only rely on service code and detect bugs
        if (availableStock > 0) {
            product.setAvailableStock(availableStock - 1);
            saveProduct(product);
        } else {
            log.warn("Trying to decrement stock <= 0");
        }
    }

    public void markOutOfStock(Product product) {
        product.setAvailableStock(0);
        saveProduct(product);
    }

    private void saveProduct(Product product) {
        productRepository.save(productMapper.toProductEntity(product));
    }
}
