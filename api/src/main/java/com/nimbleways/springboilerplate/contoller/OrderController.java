package com.nimbleways.springboilerplate.contoller;

import com.nimbleways.springboilerplate.contoller.dto.ProcessOrderResponse;
import com.nimbleways.springboilerplate.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {
    private final OrderService orderService;

    @PostMapping("{orderId}/process")
    @ResponseStatus(HttpStatus.OK)
    public ProcessOrderResponse processOrder(@PathVariable Long orderId) {
        log.info("Receiving a processOrder request for order id = {}", orderId);
        Long processedId = orderService.processOrder(orderId);
        return new ProcessOrderResponse(processedId);
    }
}
