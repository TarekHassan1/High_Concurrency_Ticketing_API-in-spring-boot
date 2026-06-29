package com.example.High_Concurrency_Ticketing_API.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.time.Instant;
import java.time.LocalDate;

public record EventDtoRequest(
        @NotBlank
        String name, Instant dateForEventToStart,

        @Min(value = 1,message = "seats must be greater than or equal to 1")
        Long totalSeats,
        Long availableSeats
) {



}
