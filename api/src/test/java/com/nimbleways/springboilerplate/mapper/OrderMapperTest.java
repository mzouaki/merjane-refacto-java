package com.nimbleways.springboilerplate.mapper;

import com.nimbleways.springboilerplate.domain.Order;
import com.nimbleways.springboilerplate.domain.Product;
import com.nimbleways.springboilerplate.repository.entity.OrderEntity;
import com.nimbleways.springboilerplate.repository.entity.ProductEntity;
import com.nimbleways.springboilerplate.utils.Annotations.UnitTest;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mapstruct.factory.Mappers;

import java.util.Set;

import static com.nimbleways.springboilerplate.domain.ProductType.NORMAL;

@UnitTest
public class OrderMapperTest {

    private final OrderMapper orderMapper = Mappers.getMapper(OrderMapper.class);

    @Test
    public void shouldMaperOrderIdAndItems() {
        Set<ProductEntity> productEntitySet = Set.of(new ProductEntity(1L, 15, 30, NORMAL, "USB Cable", null, null, null));
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setId(12L);
        orderEntity.setItems(productEntitySet);

        Order order = orderMapper.toOrder(orderEntity);

        Assertions.assertThat(order.getId()).isEqualTo(12L);
        Assertions.assertThat(order.getItems()).hasSize(1);
        Product product = order.getItems().iterator().next();
        Assertions.assertThat(product.getId()).isEqualTo(1L);
        Assertions.assertThat(product.getLeadTime()).isEqualTo(15);
        Assertions.assertThat(product.getAvailableStock()).isEqualTo(30);
        Assertions.assertThat(product.getName()).isEqualTo("USB Cable");
        Assertions.assertThat(product.getType()).isEqualTo(NORMAL);


    }
}
