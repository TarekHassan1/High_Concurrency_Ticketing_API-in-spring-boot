package com.example.High_Concurrency_Ticketing_API.exceptionHandling;

public class EventNotFoundException extends  RuntimeException{
    public EventNotFoundException(String message) {
        super(message);
    }
}
