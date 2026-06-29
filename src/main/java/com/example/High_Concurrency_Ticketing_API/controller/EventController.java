package com.example.High_Concurrency_Ticketing_API.controller;

import com.example.High_Concurrency_Ticketing_API.dto.EventDtoRequest;
import com.example.High_Concurrency_Ticketing_API.dto.EventDtoResponse;

import com.example.High_Concurrency_Ticketing_API.service.EventService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/events")
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    public ResponseEntity<EventDtoResponse> createEvent(@RequestBody @Valid EventDtoRequest eventDtoRequest) {
        EventDtoResponse eventDtoResponse = eventService.createEvent(eventDtoRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(eventDtoResponse);
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable UUID eventId) {
        eventService.deleteEvent(eventId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/{eventId}/seats")
    public ResponseEntity<EventDtoResponse> updateAvailableSeats(@PathVariable UUID eventId, @RequestParam long seatsTaken) {
        EventDtoResponse eventDtoResponse = eventService.updateAvailableSeats(eventId, seatsTaken);
        return ResponseEntity.status(HttpStatus.OK).body(eventDtoResponse);
    }

    @PatchMapping("/{eventId}/start-time/decrease")
    public ResponseEntity<EventDtoResponse> decreaseEventStartTime(@PathVariable UUID eventId, @RequestParam long time) {

        EventDtoResponse eventDtoResponse = eventService.decreaseEventStartTime(eventId, time);
        return ResponseEntity.status(HttpStatus.OK).body(eventDtoResponse);

    }

    @PatchMapping("/{eventId}/start-time/increase")
    public ResponseEntity<EventDtoResponse> increaseEventStartTime(@PathVariable UUID eventId, @RequestParam long time) {

        EventDtoResponse eventDtoResponse = eventService.increaseEventStartTime(eventId, time);
        return ResponseEntity.status(HttpStatus.OK).body(eventDtoResponse);

    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventDtoResponse> getEventDetails(@PathVariable UUID eventId) {
        EventDtoResponse eventDtoResponse = eventService.getEventDetails(eventId);
        return ResponseEntity.status(HttpStatus.OK).body(eventDtoResponse);
    }
}
