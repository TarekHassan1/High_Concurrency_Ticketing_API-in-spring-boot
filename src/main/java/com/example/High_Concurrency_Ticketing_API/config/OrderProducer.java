package com.example.High_Concurrency_Ticketing_API.config;

import com.example.High_Concurrency_Ticketing_API.dto.OrderDtoRequest;
import com.example.High_Concurrency_Ticketing_API.entity.Order;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderProducer {
    private final RabbitTemplate rabbitTemplate;

    public OrderProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
    public void BuyATicket(OrderDtoRequest request) {
        String exchange = "order.exchange";
        String routingKey = "order.routing.key";

        rabbitTemplate.convertAndSend(exchange, routingKey, request);
    }
}
