# GymManagement – REST API

A backend REST API for gym management built with Java Spring Boot.
The system handles authentication, role-based access control,
subscription management, and class booking.

---

## Tech Stack

- Java 17+
- Spring Boot 3.x
- Spring Security (JWT)
- Spring Data JPA
- MySQL
- Maven

---

## Features

- JWT authentication (register / login)
- Role-based access control with three user types:
    - **Admin** – full access: manage users, roles, subscriptions
    - **Instructor** – manage classes and view participants
    - **Client** – view profile, subscription, and book classes
- Subscription types: monthly, quarterly, annual
- Granular endpoint protection via `@PreAuthorize`

---

## API Endpoints (examples)

### Auth
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/auth/register | Register a new user |
| POST | /api/auth/login | Login and receive JWT token |

### Users (Admin only)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/users | Get all users |
| PATCH | /api/users/{id}/makeadmin | Promote user to Admin |

---

## Local Setup

1. Clone the repository:
   git clone https://github.com/ManuelDiSab/GymManagement.git

2. Create an `app.env` file in the project root with the following variables:
   JWT_SECRET=your_jwt_secret_key_min_64_chars
   ADMIN_USERNAME=your_admin_username
   ADMIN_EMAIL=your_admin_email
   ADMIN_PASSWORD=your_admin_password
   DB_PASSWORD=your_database_password

3. Make sure MySQL is running and the database exists:
   CREATE DATABASE gym_db;

4. Run the application:
   ./mvnw spring-boot:run

The application will automatically create the Super Admin user on first startup.