package com.nimbleways.springboilerplate.mapper;

import com.nimbleways.springboilerplate.domain.Order;
import com.nimbleways.springboilerplate.repository.entity.OrderEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    Order toOrder(OrderEntity orderEntity);
}
