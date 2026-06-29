package com.example.High_Concurrency_Ticketing_API.consumer;

import com.example.High_Concurrency_Ticketing_API.dto.OrderConfirmedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class NotificationListener {

    private final JavaMailSender mailSender;

    public NotificationListener(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    @EventListener
    public void handleOrderConfirmation(OrderConfirmedEvent event) {
        System.out.println("[Async Thread: " + Thread.currentThread().getName()
                + "] Beginning ticket layout dispatch for user: " + event.email());

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("noreply@flashtick.com");
            message.setTo(event.email());
            message.setSubject("Your Ticket Booking Confirmation - FlashTick");
            message.setText("Congratulations!\n\n" +
                    "Your order has been processed successfully.\n" +
                    "Order ID: " + event.orderId() + "\n" +
                    "Event ID: " + event.eventId() + "\n\n" +
                    "Tracking ID: " + event.trackingId() + "\n\n\n" +
                    "Thank you for ordering with guest checkout!");

            mailSender.send(message);

            System.out.println("[Async Worker] Email successfully delivered to: " + event.email());
        } catch (Exception e) {
            System.err.println("Failed to deliver transactional notification email to "
                    + event.email() + " Error: " + e.getMessage());
        }
    }
}