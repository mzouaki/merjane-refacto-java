package com.nimbleways.springboilerplate.service;

import com.nimbleways.springboilerplate.domain.Order;
import com.nimbleways.springboilerplate.exception.OrderNotFoundException;
import com.nimbleways.springboilerplate.mapper.OrderMapper;
import com.nimbleways.springboilerplate.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final OrderMapper orderMapper;

    private Order getOrderOrThrow(Long orderId) {
        return orderMapper.toOrder(orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId)));
    }

    public Long processOrder(Long orderId) {
        Order order = getOrderOrThrow(orderId);
        log.info("Processing order {} with {} items", order.getId(), order.getItems().size());
        productService.processProducts(order.getItems());
        return order.getId();
    }
}
