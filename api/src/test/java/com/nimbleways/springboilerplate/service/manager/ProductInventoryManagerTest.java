package com.nimbleways.springboilerplate.service.manager;

import com.nimbleways.springboilerplate.domain.Product;
import com.nimbleways.springboilerplate.mapper.ProductMapper;
import com.nimbleways.springboilerplate.repository.ProductRepository;
import com.nimbleways.springboilerplate.repository.entity.ProductEntity;
import com.nimbleways.springboilerplate.utils.Annotations.UnitTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.nimbleways.springboilerplate.domain.ProductType.NORMAL;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@UnitTest
public class ProductInventoryManagerTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductMapper productMapper;
    @InjectMocks
    private ProductInventoryManager productInventoryManager;

    @Test
    public void shouldDecrementStockAndSave() {

        Product product = new Product(1L, 15, 3, NORMAL, "RJ45 Cable", null, null, null);
        ProductEntity productEntity = new ProductEntity(1L, 15, 2, NORMAL, "RJ45 Cable", null, null, null);

        when(productMapper.toProductEntity(product)).thenReturn(productEntity);

        productInventoryManager.decrementStock(product);

        Assertions.assertThat(product.getAvailableStock()).isEqualTo(2);
        verify(productMapper).toProductEntity(product);
        verify(productRepository).save(productEntity);
    }


}