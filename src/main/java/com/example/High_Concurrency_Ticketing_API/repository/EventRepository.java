package com.example.High_Concurrency_Ticketing_API.repository;

import com.example.High_Concurrency_Ticketing_API.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {
}
