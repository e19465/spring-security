# Spring Boot Authentication API

## Introduction
This project is a **Spring Boot** backend API that demonstrates a **complete authentication flow** using **Spring Security**, **JWT**, and **email-based OTP verification**. It follows best practices for security and API design, including role-based authentication and secure HTTP-only cookies.

## Features
- **Java Spring Boot Backend API**
- **MySQL Database Integration**
- **Email Service** using Java Mail Sender for OTP-based authentication
- **Complete Authentication Flow**:
    - Email verification using OTP
    - Password reset using OTP
- **Spring Security with JWT Authentication**
- **Custom CORS Configuration**
- **Role-Based Authentication (Public, User, Admin APIs)**
- **Best Practices** for security and maintainability

## Technologies Used
- **Spring Boot** (Backend framework)
- **Spring Security** (Authentication and authorization)
- **JWT (JSON Web Token)** (Token-based authentication)
- **Java Mail Sender** (Email verification and OTP handling)
- **MySQL** (Relational database for user management)
- **Spring Data JPA** (Database interaction)
- **Lombok** (Java library for boilerplate code reduction)
- **Spring data JPA** (Database interaction)
- **Maven** (Dependency management)
- **Postman** (API testing)
- **IntelliJ IDEA** (IDE)
- **Git** (Version control)

## Security & Authentication
- Uses **JWT tokens** stored in **HTTP-only cookies** for authentication.
- Implements **role-based authentication** (USER, ADMIN).
- OTP-based **email verification** and **password reset**.
- **Spring Security filters** to secure endpoints.

## Setup & Installation
### Prerequisites
- **Java 21**
- **MySQL Database**
- **Postman** (For API testing)
- **IntelliJ IDEA IDE** (or any other preferred)

### Steps to Run
1. Clone the repository:
   ```sh
   git clone https://github.com/e19465/spring-security.git
   cd your-repo
   ```
2. Run the application:
   ```sh
   mvn spring-boot:run
   ```
3. Access API at `http://localhost:9091/api/v1`

## Best Practices Implemented
- **Secure Password Storage** using BCrypt
- **JWT stored in HTTP-only cookies** for security
- **Proper exception handling**
- **Role-based access control (RBAC)**
- **Spring Security best practices**


---
### 🚀 Developed with **Spring Boot & Security Best Practices**
