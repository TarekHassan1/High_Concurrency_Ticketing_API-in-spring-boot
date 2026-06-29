package com.example.High_Concurrency_Ticketing_API.service;

import com.example.High_Concurrency_Ticketing_API.dto.EventDtoRequest;
import com.example.High_Concurrency_Ticketing_API.dto.EventDtoResponse;
import com.example.High_Concurrency_Ticketing_API.entity.Event;
import com.example.High_Concurrency_Ticketing_API.exceptionHandling.EventNotFoundException;
import com.example.High_Concurrency_Ticketing_API.exceptionHandling.InsufficientSeatsException;
import com.example.High_Concurrency_Ticketing_API.mapper.ProjectMapper;
import com.example.High_Concurrency_Ticketing_API.repository.EventRepository;
import jakarta.validation.Valid;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class EventService {
    private final  EventRepository eventRepository;
    private final ProjectMapper mapper;
    public EventService(EventRepository eventRepository, ProjectMapper mapper) {
        this.eventRepository = eventRepository;
        this.mapper = mapper;
    }
    public EventDtoResponse createEvent(@Valid  EventDtoRequest eventDtoRequest){
        Event event = mapper.dtoEventToEntity(eventDtoRequest);
        eventRepository.save(event);
        return mapper.eventToDto(event);

    }
    @Cacheable(value = "events", key = "#eventId")
    public EventDtoResponse getEventDetails(UUID eventId){

        Event event = eventRepository.findById(eventId).orElseThrow(()-> new EventNotFoundException("Event not found with id "+eventId));
        return mapper.eventToDto(event);

    }
    @CacheEvict(value = "events", key = "#eventId")
    @Transactional
    public EventDtoResponse decreaseEventStartTime(UUID eventId,long time){

        Event event =eventRepository.findById(eventId).orElseThrow(()-> new EventNotFoundException("Event not found with id "+eventId));

        event.setDateForEventToStart(event.getDateForEventToStart().minusSeconds(time));
        eventRepository.save(event);
        return mapper.eventToDto(event);

    }

    @Transactional(noRollbackFor = InsufficientSeatsException.class)
    @CacheEvict(value = "events", key = "#eventId")
    public EventDtoResponse updateAvailableSeats(UUID eventId,long seatsTaken)  {

        Event event =eventRepository.findById(eventId).orElseThrow(()-> new EventNotFoundException("Event not found with id "+eventId));
        if(event.getAvailableSeats()-seatsTaken<0){
            throw new InsufficientSeatsException("Not Enough Seats");
        }
        event.setAvailableSeats(event.getAvailableSeats()-seatsTaken);
        eventRepository.save(event);
        return mapper.eventToDto(event);
    }
    @Transactional
    @CacheEvict(value = "events", key = "#eventId")
    public EventDtoResponse increaseEventStartTime(UUID eventId,long time){

        Event event = eventRepository.findById(eventId).orElseThrow(()-> new EventNotFoundException("Event not found with id "+eventId));
        event.setDateForEventToStart(event.getDateForEventToStart().plusSeconds(time));
        eventRepository.save(event);
        return mapper.eventToDto(event);

    }
    @Transactional
    @CacheEvict(value = "events", key = "#eventId")
    public void deleteEvent(UUID eventId){
        Event event = eventRepository.findById(eventId).orElseThrow(()-> new EventNotFoundException("Event not found with id "+eventId));
        eventRepository.deleteById(eventId);

    }

}
