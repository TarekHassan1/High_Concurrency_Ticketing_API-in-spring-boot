package com.example.High_Concurrency_Ticketing_API.consumer;
import com.example.High_Concurrency_Ticketing_API.dto.OrderDtoRequest;
import com.example.High_Concurrency_Ticketing_API.entity.Status;
import com.example.High_Concurrency_Ticketing_API.service.BookingFacade;
import com.example.High_Concurrency_Ticketing_API.config.RabbitMQConfig;
import com.example.High_Concurrency_Ticketing_API.service.OrderService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class OrderConsumer {
    private final StringRedisTemplate redisTemplate;
    private final BookingFacade bookingFacade;
    public OrderConsumer(BookingFacade bookingFacade, OrderService orderService, StringRedisTemplate redisTemplate) {
        this.bookingFacade = bookingFacade;
        this.redisTemplate = redisTemplate;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void processOrder(OrderDtoRequest request) {
        String redisKey = "order:status:" + request.trackingId();
        try {
            bookingFacade.bookTicket(request);
        } finally {
            redisTemplate.delete(redisKey);
        }
    }
}