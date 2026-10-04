package com.nimbleways.springboilerplate.controller;

import com.nimbleways.springboilerplate.repository.OrderRepository;
import com.nimbleways.springboilerplate.repository.ProductRepository;
import com.nimbleways.springboilerplate.repository.entity.OrderEntity;
import com.nimbleways.springboilerplate.repository.entity.ProductEntity;
import com.nimbleways.springboilerplate.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.nimbleways.springboilerplate.domain.ProductType.EXPIRABLE;
import static com.nimbleways.springboilerplate.domain.ProductType.NORMAL;
import static com.nimbleways.springboilerplate.domain.ProductType.SEASONAL;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// import com.fasterxml.jackson.databind.ObjectMapper;

// Specify the controller class you want to test
// This indicates to spring boot to only load UsersController into the context
// Which allows a better performance and needs to do less mocks
@SpringBootTest
@AutoConfigureMockMvc
public class OrderController {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService notificationService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    private static OrderEntity createOrder(Set<ProductEntity> products) {
        OrderEntity order = new OrderEntity();
        order.setItems(products);
        return order;
    }

    private static List<ProductEntity> createProducts() {
        List<ProductEntity> products = new ArrayList<>();
        products.add(new ProductEntity(null, 15, 30, NORMAL, "USB Cable", null, null, null));
        products.add(new ProductEntity(null, 10, 0, NORMAL, "USB Dongle", null, null, null));
        products.add(new ProductEntity(null, 15, 30, EXPIRABLE, "Butter", LocalDate.now().plusDays(26), null,
                null));
        products.add(new ProductEntity(null, 90, 6, EXPIRABLE, "Milk", LocalDate.now().minusDays(2), null, null));
        products.add(new ProductEntity(null, 15, 30, SEASONAL, "Watermelon", null, LocalDate.now().minusDays(2),
                LocalDate.now().plusDays(58)));
        products.add(new ProductEntity(null, 15, 30, SEASONAL, "Grapes", null, LocalDate.now().plusDays(180),
                LocalDate.now().plusDays(240)));
        return products;
    }

    @Test
    public void processOrderShouldReturn() throws Exception {
        List<ProductEntity> allProducts = createProducts();
        Set<ProductEntity> orderItems = new HashSet<ProductEntity>(allProducts);
        OrderEntity order = createOrder(orderItems);
        productRepository.saveAll(allProducts);
        order = orderRepository.save(order);
        mockMvc.perform(post("/orders/{orderId}/process", order.getId())
                        .contentType("application/json"))
                .andExpect(status().isOk());
        OrderEntity resultOrder = orderRepository.findById(order.getId()).get();
        assertEquals(resultOrder.getId(), order.getId());
    }
}
