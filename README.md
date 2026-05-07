Gym Management System – REST API (Spring Boot)
Backend REST API for comprehensive gym management, designed with a scalable and secure architecture.

🔥 Overview
This project simulates a real-world system used by a gym to manage:

Users with different roles

Memberships

Classes and bookings

The goal is to replicate realistic business logic and backend-side permission management.

🧠 Key Features
JWT Authentication with Spring Security

Role Management (Admin, Instructor, Client) with granular authorizations

Membership Management (monthly, quarterly, annual)

Class Booking System

Entity Relationships managed via JPA (OneToMany, ManyToOne)

🏗 Architecture
The project follows a layered structure:

Controller → handles HTTP requests

Service → business logic

Repository → data access (Spring Data JPA)

DTOs (Data Transfer Objects) are used to decouple internal models from the APIs.

🔐 Security
Authentication via JWT

Authorization via @PreAuthorize

Endpoint protection based on user roles

🛠 Tech Stack
Java 17

Spring Boot 3

Spring Security (JWT)

Spring Data JPA

MySQL

Maven

⚙️ Setup
Clone the repository

Configure MySQL

Run with:
mvn spring-boot:run