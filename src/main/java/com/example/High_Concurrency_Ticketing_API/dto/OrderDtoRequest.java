package com.example.High_Concurrency_Ticketing_API.dto;

import com.example.High_Concurrency_Ticketing_API.entity.Status;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record OrderDtoRequest (

         UUID eventId,
         UUID trackingId,
         Status status,
         @Min(value = 1,message = "ordered seats must be greater than or equal to 1")
         Long seatCount
        ,
         @NotBlank(message = "Email is required for ticket delivery")
         @Email(message = "Please provide a valid email address")
         String email
){
}
