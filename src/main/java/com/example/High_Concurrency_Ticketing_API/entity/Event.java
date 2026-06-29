package com.example.High_Concurrency_Ticketing_API.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy=GenerationType.UUID)
    private UUID id ;
    private String name;
    private Instant dateForEventToStart;
    private Long totalSeats;
    private Long availableSeats;
    @Version
    private Long version;

}
