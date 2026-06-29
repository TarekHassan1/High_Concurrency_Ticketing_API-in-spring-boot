package com.example.High_Concurrency_Ticketing_API.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy=GenerationType.UUID)
    private UUID id ;

    @Column(unique = true, nullable = false, updatable = false)
    private UUID trackingId ;
    @Enumerated(EnumType.STRING)
    private Status status;
   // private UUID userId;
    private UUID eventId;
    private String email;
    private Long seatCount;

    private Instant createdAt;


}
