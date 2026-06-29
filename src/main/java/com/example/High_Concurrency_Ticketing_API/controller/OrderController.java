package com.example.High_Concurrency_Ticketing_API.controller;

import com.example.High_Concurrency_Ticketing_API.config.OrderProducer;
import com.example.High_Concurrency_Ticketing_API.dto.OrderDtoRequest;
import com.example.High_Concurrency_Ticketing_API.entity.Order;
import com.example.High_Concurrency_Ticketing_API.entity.Status;
import com.example.High_Concurrency_Ticketing_API.exceptionHandling.OrderNotFoundException;
import com.example.High_Concurrency_Ticketing_API.repository.OrderRepository;
import com.example.High_Concurrency_Ticketing_API.service.BookingFacade;
import com.example.High_Concurrency_Ticketing_API.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
    private final OrderService orderService;
    private final OrderRepository orderRepository;
    private final BookingFacade bookingFacade;
    private final OrderProducer orderProducer;
    private final StringRedisTemplate redisTemplate;
    public OrderController(OrderService orderService, OrderRepository orderRepository, BookingFacade bookingFacade, OrderProducer orderProducer, StringRedisTemplate redisTemplate) {
        this.orderService = orderService;
        this.orderRepository = orderRepository;
        this.bookingFacade = bookingFacade;
        this.orderProducer = orderProducer;
        this.redisTemplate = redisTemplate;
    }
    @GetMapping("/status/{trackingId}")
    public ResponseEntity<Status> checkOrderStatus(@PathVariable UUID trackingId) {
        String redisKey = "order:status:" + trackingId;
        String cachedStatus = redisTemplate.opsForValue().get(redisKey);

        if (cachedStatus != null) {
            return ResponseEntity.ok(Status.valueOf(cachedStatus));
        }
        Order order = orderRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new OrderNotFoundException("Tracking ID not found: " + trackingId));

        return ResponseEntity.ok(order.getStatus());
    }
    @PostMapping
    public ResponseEntity<String> orderATicket( @RequestBody @Valid OrderDtoRequest orderDtoRequest){
        UUID trackingId=UUID.randomUUID();
        OrderDtoRequest request= new OrderDtoRequest(
                orderDtoRequest.eventId(),
                trackingId,
                Status.PENDING,
                orderDtoRequest.seatCount(),
                orderDtoRequest.email()
        );
        String redisKey = "order:status:" + trackingId;
        redisTemplate.opsForValue().set(redisKey, Status.PENDING.name(), Duration.ofMinutes(10));
        orderProducer.BuyATicket(request);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Order submitted successfully. Your request is being processed. tracking id: "+ trackingId);
    }
    @PatchMapping("/{orderId}")
    public ResponseEntity<String> refundATicket(@PathVariable UUID orderId){
        String result = orderService.refundATicket(orderId);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
