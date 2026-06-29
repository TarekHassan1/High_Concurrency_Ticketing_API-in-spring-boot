package com.example.High_Concurrency_Ticketing_API.dto;

import java.time.LocalDateTime;

public record ErrorResponse(
         LocalDateTime timestamp,
         int status,
         String error,
         String message
) {
}
