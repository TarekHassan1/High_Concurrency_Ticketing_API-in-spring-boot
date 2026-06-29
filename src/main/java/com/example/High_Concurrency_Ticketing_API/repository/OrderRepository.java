package com.example.High_Concurrency_Ticketing_API.repository;

import com.example.High_Concurrency_Ticketing_API.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    Optional<Order> findByTrackingId(UUID trackingId);
}
