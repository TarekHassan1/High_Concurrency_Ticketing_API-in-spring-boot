package com.example.High_Concurrency_Ticketing_API.exceptionHandling;

public class InsufficientSeatsException extends  RuntimeException{
    public InsufficientSeatsException(String message){
        super(message);
    }
}
