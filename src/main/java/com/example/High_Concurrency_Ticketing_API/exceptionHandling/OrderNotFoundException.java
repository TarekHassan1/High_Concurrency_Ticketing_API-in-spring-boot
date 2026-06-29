package com.example.High_Concurrency_Ticketing_API.exceptionHandling;

public class OrderNotFoundException extends RuntimeException{
   public  OrderNotFoundException(String message){
       super(message);
   }
}
