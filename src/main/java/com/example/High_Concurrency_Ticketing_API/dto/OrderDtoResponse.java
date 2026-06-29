package com.example.High_Concurrency_Ticketing_API.dto;

import com.example.High_Concurrency_Ticketing_API.entity.Status;
import jakarta.validation.constraints.Min;

import java.time.Instant;
import java.util.UUID;

public record OrderDtoResponse(
        UUID eventId,

        @Min(value = 1,message = "ordered seats must be greater than or equal to 1")
        Long seatCount,
        UUID trackingId,
        Status status,
        Instant createdAt
) {
}
