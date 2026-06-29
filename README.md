# High-Concurrency Ticketing API

A high-performance, event-driven ticket booking engine built with **Spring Boot 3**, designed to handle flash-sale scenarios under extreme concurrent traffic conditions. By decoupling transactional ingestion from downstream persistence layers, this system eliminates typical relational database bottlenecks, delivering sub-150ms response times.

---

## 🏗️ Architectural Blueprint & Data Flow

The core design principle of this API is **Asynchronous Decoupling and Event-Driven Processing**. Instead of forcing incoming client requests to wait for synchronous database row locks, transaction verification, and transactional email dispatches, requests are ingested and acknowledged immediately.
