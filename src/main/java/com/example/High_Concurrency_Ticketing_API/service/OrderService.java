package com.example.High_Concurrency_Ticketing_API.service;

import com.example.High_Concurrency_Ticketing_API.dto.OrderConfirmedEvent;
import com.example.High_Concurrency_Ticketing_API.dto.OrderDtoRequest;
import com.example.High_Concurrency_Ticketing_API.dto.OrderDtoResponse;
import com.example.High_Concurrency_Ticketing_API.entity.Event;
import com.example.High_Concurrency_Ticketing_API.entity.Order;
import com.example.High_Concurrency_Ticketing_API.entity.Status;
import com.example.High_Concurrency_Ticketing_API.exceptionHandling.EventNotFoundException;
import com.example.High_Concurrency_Ticketing_API.exceptionHandling.InsufficientSeatsException;
import com.example.High_Concurrency_Ticketing_API.exceptionHandling.OrderNotFoundException;
import com.example.High_Concurrency_Ticketing_API.mapper.ProjectMapper;
import com.example.High_Concurrency_Ticketing_API.repository.EventRepository;
import com.example.High_Concurrency_Ticketing_API.repository.OrderRepository;

import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class OrderService {
    private final ProjectMapper mapper;
    private final OrderRepository orderRepository;
    private final EventRepository eventRepository;
    private final EventService eventService;
    private final ApplicationEventPublisher eventPublisher;
    public OrderService(ProjectMapper mapper, OrderRepository orderRepository, EventRepository eventRepository, EventService eventService, ApplicationEventPublisher eventPublisher) {
        this.mapper = mapper;
        this.orderRepository = orderRepository;
        this.eventRepository = eventRepository;
        this.eventService = eventService;
        this.eventPublisher = eventPublisher;
    }


    @Transactional
    public OrderDtoResponse orderATicket(OrderDtoRequest orderDtoRequest) {
        Order order =  mapper.dtoOrderToEntity(orderDtoRequest);

        try {

            Event event = eventRepository.findById(orderDtoRequest.eventId())
                    .orElseThrow(() -> new EventNotFoundException("Event not Found With id " + orderDtoRequest.eventId()));

            event.setAvailableSeats(event.getAvailableSeats()-orderDtoRequest.seatCount());
            eventRepository.save(event);

            order.setStatus(Status.CONFIRMED);
            eventPublisher.publishEvent(new OrderConfirmedEvent(
                    order.getId(),
                    order.getEventId(),
                    orderDtoRequest.trackingId(),
                    orderDtoRequest.email()
            ));
        } catch (InsufficientSeatsException e) {
            order.setStatus(Status.FAILED);
        }

        orderRepository.save(order);

        return mapper.orderToDto(order);
    }

    @Transactional
    public String refundATicket(UUID orderId){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id " + orderId));

        if (order.getStatus() == Status.REFUNDED) {
            return "This ticket has already been refunded.";
        }

        Event event = eventRepository.findById(order.getEventId())
                .orElseThrow(() -> new EventNotFoundException("Event not Found With id " + order.getEventId()));

        event.setAvailableSeats(event.getAvailableSeats() + order.getSeatCount());
        order.setStatus(Status.REFUNDED);

        eventRepository.save(event);
        orderRepository.save(order);
        return "Refunded Successfully";
    }


}