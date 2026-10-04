package com.nimbleways.springboilerplate.mapper;

import com.nimbleways.springboilerplate.domain.Product;
import com.nimbleways.springboilerplate.repository.entity.ProductEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductEntity toProductEntity(Product product);
}
