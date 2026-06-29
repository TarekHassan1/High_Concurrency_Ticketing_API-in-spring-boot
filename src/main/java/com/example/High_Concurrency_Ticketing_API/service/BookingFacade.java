package com.example.High_Concurrency_Ticketing_API.service;

import com.example.High_Concurrency_Ticketing_API.dto.OrderDtoRequest;
import com.example.High_Concurrency_Ticketing_API.dto.OrderDtoResponse;
import com.example.High_Concurrency_Ticketing_API.exceptionHandling.InsufficientSeatsException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

@Component
public class BookingFacade {

    private final OrderService orderService;

    public BookingFacade(OrderService orderService) {
        this.orderService = orderService;
    }

    @Retryable(
            retryFor = OptimisticLockingFailureException.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 50)
    )
    public void bookTicket(OrderDtoRequest orderDtoRequest) {
         orderService.orderATicket(orderDtoRequest);
    }

    @Recover
    public OrderDtoResponse recoverFromOptimisticLock(OptimisticLockingFailureException ex, OrderDtoRequest orderDtoRequest) {
        throw new InsufficientSeatsException("Server is busy processing transactions. Please try submitting your order again.");
    }
}