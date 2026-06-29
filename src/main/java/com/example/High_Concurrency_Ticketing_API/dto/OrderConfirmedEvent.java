package com.example.High_Concurrency_Ticketing_API.dto;

import java.util.UUID;

public record OrderConfirmedEvent(UUID orderId, UUID eventId,UUID trackingId, String email) {}