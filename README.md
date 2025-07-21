BuyAnytime E-Commerce Platform 🚀
Introduction 🌟
Welcome to BuyAnytime, a modern, cloud-native e-commerce backend built on a robust microservices architecture. This platform provides a scalable, resilient, and maintainable foundation for a complete online retail business.

This project is designed to be developer-friendly, leveraging Docker for infrastructure management and Spring Boot for rapid service development. It showcases a complete, event-driven ecosystem featuring an API Gateway, Service Registry, and asynchronous messaging with Kafka, providing a powerful and efficient system for handling e-commerce operations.

System Architecture 🛠️
The BuyAnytime platform is composed of several independent microservices that work together to provide a seamless user experience. The architecture is designed for high availability and independent scalability of each component.

New Architecture Diagram
This diagram provides a detailed view of all services, their core technologies, and how they communicate.

<img width="3840" height="2368" alt="BuyAnytimeArchitecture" src="https://github.com/user-attachments/assets/19bfe1eb-ad76-470e-a1fb-06065baa6a65" />


Microservice Breakdown
Service

Description

Key Technologies

API Gateway

The single, secure entry point for all client requests. Manages routing, security (JWT validation), and rate limiting.

Spring Cloud Gateway

Service Registry

Allows services to dynamically find and communicate with each other without hardcoded URLs.

Netflix Eureka

Identity Service

Handles all user-related operations: registration, login (authentication), and role-based access control (authorization).

Spring Security, JWT, MySQL

Product Service

Manages the entire product catalog, including categories, variants, pricing, and inventory. Uses Redis for caching.

Spring Boot, MySQL, Redis, Cloudinary

Order Service

Orchestrates the checkout process, validates product stock, calculates totals, and initiates payment flows with Razorpay.

Spring Boot, MySQL, Razorpay SDK

Payment Service

Listens for order events from Kafka to create and track payment records. Manages payment statuses.

Spring Boot, MySQL, Kafka Consumer

Email Service

Listens for order events from Kafka to send transactional emails, such as order confirmations.

Spring Boot, Spring Mail, Kafka Consumer

Technology Stack 🔧
Framework: Spring Boot 3 & Spring Cloud

Communication: REST APIs (OpenFeign) & Asynchronous Messaging (Apache Kafka)

Database: MySQL (for persistent data) & Redis (for caching)

Authentication: Spring Security with JWT (JSON Web Tokens)

Payments: Razorpay SDK

Image Storage: Cloudinary

Service Discovery: Netflix Eureka

API Gateway: Spring Cloud Gateway

Distributed Tracing: Zipkin

Containerization: Docker & Docker Compose

Project Setup Guide 🛠️
This project can be run in two primary modes: a development mode using your IDE and Docker, or a production-like mode running entirely on Docker.

🛠️ Prerequisites
Java 17 JDK: Ensure your IDE and system are configured to use Java 17.

Docker & Docker Compose: Install Docker Desktop

IntelliJ IDEA: Recommended for running the services.

Postman: For API testing.

🚀 Development Setup (IDE + Docker)
This is the recommended approach for local development and debugging.

1. Start Infrastructure Services:
First, start all the background services (MySQL, Kafka, Redis, etc.) using Docker. From the project's root directory (BuyAnytime), run:

docker-compose up -d

2. Create Databases:
Connect to the running MySQL container and create the necessary databases.

# Connect to the container
docker exec -it mysql mysql -u root -p

# Inside the MySQL prompt, run these commands:
CREATE DATABASE identity_db;
CREATE DATABASE order_db;
CREATE DATABASE payment_db;
EXIT;

3. Run Microservices in IntelliJ IDEA:
Open the project in IntelliJ and run each microservice by right-clicking its main application class. Start them in this order:

service-registry

api-gateway

identity-service

product-service

order-service

payment-service

email-service

4. Verify Services:

Open the Eureka Dashboard at http://localhost:8761 to see all services registered.

Use the Services tool window in IntelliJ to see all running applications.

🐳 Production Setup (Fully Dockerized)
To run the entire application (including all Java services) inside Docker, you will need to use the original docker-compose.yml file from the source project, which includes build contexts for each service.

From the project's root directory, execute:

docker-compose up --build -d

API Testing with Postman 🔍
A complete Postman collection has been created to test all API endpoints. You can import it to get started quickly.

API Gateway URL: http://localhost:9191

Authentication: Most endpoints are secured. First, run the Register and Login requests from the Identity Service folder to get a JWT. Store this token in the jwt_token collection variable to automatically authorize subsequent requests.

Key API Flows:
Register & Login: POST /api/v1/auth/register and POST /api/v1/auth/token

Create a Product: POST /api/v1/products (Requires ADMIN role)

Create a Razorpay Order: POST /api/v1/order/create-payment-order

Place an Order: POST /api/v1/order (Requires successful payment verification)
