package com.example.High_Concurrency_Ticketing_API.mapper;

import com.example.High_Concurrency_Ticketing_API.dto.EventDtoRequest;
import com.example.High_Concurrency_Ticketing_API.dto.EventDtoResponse;
import com.example.High_Concurrency_Ticketing_API.dto.OrderDtoRequest;
import com.example.High_Concurrency_Ticketing_API.dto.OrderDtoResponse;
import com.example.High_Concurrency_Ticketing_API.entity.Event;
import com.example.High_Concurrency_Ticketing_API.entity.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProjectMapper {
    EventDtoResponse eventToDto(Event event);
    Event dtoEventToEntity(EventDtoRequest eventDtoRequest);

    OrderDtoResponse orderToDto(Order order);
    Order dtoOrderToEntity(OrderDtoRequest orderDtoRequest);


}
