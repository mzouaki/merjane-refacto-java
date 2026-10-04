package com.nimbleways.springboilerplate.mapper;

import com.nimbleways.springboilerplate.domain.Product;
import com.nimbleways.springboilerplate.repository.entity.ProductEntity;
import com.nimbleways.springboilerplate.utils.Annotations.UnitTest;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;

import static com.nimbleways.springboilerplate.domain.ProductType.EXPIRABLE;
import static com.nimbleways.springboilerplate.domain.ProductType.NORMAL;
import static com.nimbleways.springboilerplate.domain.ProductType.SEASONAL;

@UnitTest
public class ProductMapperTest {

    private final ProductMapper productMapper = Mappers.getMapper(ProductMapper.class);

    @Test
    public void shouldMapNormalProduct() {
        Product product = new Product(1L, 15, 30, NORMAL, "USB Cable", null, null, null);

        ProductEntity productEntity = productMapper.toProductEntity(product);

        Assertions.assertThat(productEntity.getId()).isEqualTo(1L);
        Assertions.assertThat(productEntity.getLeadTime()).isEqualTo(15);
        Assertions.assertThat(productEntity.getAvailableStock()).isEqualTo(30);
        Assertions.assertThat(productEntity.getName()).isEqualTo("USB Cable");
        Assertions.assertThat(productEntity.getType()).isEqualTo(NORMAL);
    }

    @Test
    public void shouldMapSeasonalProduct() {
        LocalDate seasonStart = LocalDate.of(2026, 10, 4);
        LocalDate seasonEnd = LocalDate.of(2026, 12, 10);
        Product product = new Product(1L, 15, 30, SEASONAL, "Apple", null, seasonStart, seasonEnd);

        ProductEntity productEntity = productMapper.toProductEntity(product);

        Assertions.assertThat(productEntity.getId()).isEqualTo(1L);
        Assertions.assertThat(productEntity.getLeadTime()).isEqualTo(15);
        Assertions.assertThat(productEntity.getAvailableStock()).isEqualTo(30);
        Assertions.assertThat(productEntity.getName()).isEqualTo("Apple");
        Assertions.assertThat(productEntity.getType()).isEqualTo(SEASONAL);
        Assertions.assertThat(productEntity.getSeasonStartDate()).isEqualTo(seasonStart);
        Assertions.assertThat(productEntity.getSeasonEndDate()).isEqualTo(seasonEnd);
    }

    @Test
    public void shouldMapExpirableProduct() {
        LocalDate expiryDate = LocalDate.of(2026, 10, 4);
        Product product = new Product(1L, 15, 30, EXPIRABLE, "Butter", expiryDate, null, null);

        ProductEntity productEntity = productMapper.toProductEntity(product);

        Assertions.assertThat(productEntity.getId()).isEqualTo(1L);
        Assertions.assertThat(productEntity.getLeadTime()).isEqualTo(15);
        Assertions.assertThat(productEntity.getAvailableStock()).isEqualTo(30);
        Assertions.assertThat(productEntity.getName()).isEqualTo("Butter");
        Assertions.assertThat(productEntity.getType()).isEqualTo(EXPIRABLE);
        Assertions.assertThat(productEntity.getExpiryDate()).isEqualTo(expiryDate);
    }
}
