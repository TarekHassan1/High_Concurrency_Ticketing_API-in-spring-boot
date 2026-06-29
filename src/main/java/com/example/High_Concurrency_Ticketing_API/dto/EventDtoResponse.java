package com.example.High_Concurrency_Ticketing_API.dto;


import java.time.Instant;
import java.util.UUID;


public record EventDtoResponse (
        UUID id,
        String name,
        Instant dateForEventToStart,
         Long totalSeats,
         Long availableSeats
        ){

}
