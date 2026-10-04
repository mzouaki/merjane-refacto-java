package com.nimbleways.springboilerplate.service;

import com.nimbleways.springboilerplate.domain.Order;
import com.nimbleways.springboilerplate.domain.Product;
import com.nimbleways.springboilerplate.exception.OrderNotFoundException;
import com.nimbleways.springboilerplate.mapper.OrderMapper;
import com.nimbleways.springboilerplate.repository.OrderRepository;
import com.nimbleways.springboilerplate.repository.entity.OrderEntity;
import com.nimbleways.springboilerplate.utils.Annotations.UnitTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static com.nimbleways.springboilerplate.domain.ProductType.NORMAL;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@UnitTest
@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductService productService;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderService orderService;

    @Test
    public void shouldProcessOrderAndReturnId() {
        Set<Product> items = Set.of(new Product(1L, 15, 3, NORMAL, "RJ45 Cable", null, null, null));
        OrderEntity orderEntity = new OrderEntity(12L, null);

        when(orderMapper.toOrder(orderEntity)).thenReturn(new Order(12L, items));
        when(orderRepository.findById(12L)).thenReturn(Optional.of(orderEntity));

        Long processedId = orderService.processOrder(12L);

        Assertions.assertThat(processedId).isEqualTo(12L);
        verify(productService).processProducts(items);
    }

    public void shouldThrowExceptionWhenNotFound() {
        when(orderRepository.findById(99L)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> orderService.processOrder((99L))).isInstanceOf(OrderNotFoundException.class);
        verifyNoInteractions(productService, orderMapper);
    }
}
