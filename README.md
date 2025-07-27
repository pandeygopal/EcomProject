BuyAnytime E-Commerce Platform
🔍 Description
BuyAnytime is a complete, cloud-native e-commerce backend built on a modern microservices architecture. This project provides a scalable, resilient, and maintainable foundation for a full-featured online retail business, showcasing an event-driven ecosystem designed for high performance and independent deployment of its components.

🚀 Features
User Authentication & Authorization: Secure registration and login using JWT, with role-based access control (Customer, Admin, Employee).

Product Catalog Management: Full CRUD operations for products, categories, and attributes, including image uploads to Cloudinary.

High-Performance Caching: Redis is used to cache product and order data, significantly reducing database load and speeding up read operations.

Asynchronous Order Processing: Apache Kafka is used to decouple services. When an order is placed, an OrderEvent is published, allowing payment and email services to react asynchronously.

Secure Payment Integration: A complete, two-step payment flow integrated with the Razorpay payment gateway.

Service Discovery & API Gateway: All services are registered with Eureka for dynamic discovery, with a single entry point managed by Spring Cloud Gateway.

Distributed Tracing: End-to-end request tracing with Zipkin to monitor and debug interactions across the distributed system.

Containerized Environment: The entire infrastructure and all microservices are fully containerized with Docker for consistent and isolated environments.

📸 System Architecture
This diagram provides a detailed view of all services, their core technologies, and how they communicate.

<img width="3840" height="2368" alt="BuyAnytimeArchitecture" src="https://github.com/user-attachments/assets/19bfe1eb-ad76-470e-a1fb-06065baa6a65" />

🛠️ Tech Stack
Backend: Java 17, Spring Boot 3, Spring Cloud

Database: MySQL (Persistence), Redis (Caching)

Messaging: Apache Kafka

Authentication: Spring Security, JSON Web Tokens (JWT)

Communication: REST APIs (OpenFeign)

Payments: Razorpay SDK

Image Storage: Cloudinary

DevOps: Docker, Docker Compose

Monitoring: Zipkin

🧪 Installation
This guide covers the recommended setup for local development using an IDE and Docker.

Prerequisites
Java 17 JDK

Maven 3.8+

Docker & Docker Compose

An IDE (like IntelliJ IDEA)

A tool for API testing (like Postman)

Steps
Clone the repository:

git clone https://github.com/pandeygopal/EcomProject.git
cd BuyAnytime

Configure API Keys:
You will need to get API keys for Razorpay, Cloudinary, and an SMTP provider (like a Gmail App Password). Add these to the respective application.properties files in each microservice.

Start Infrastructure Services:
Run the background services (MySQL, Kafka, Redis, Zipkin) using Docker.

docker-compose up -d

Create Databases:
Connect to the running MySQL container to create the necessary databases.

# Connect to the container
docker exec -it mysql mysql -u root -p

# Inside the MySQL prompt, run these commands:
CREATE DATABASE identity_db;
CREATE DATABASE order_db;
CREATE DATABASE payment_db;
EXIT;

Build the Project:
Compile all modules and install dependencies using Maven from the root directory.

mvn clean install

Run the Microservices:
Start each microservice from your IDE in the correct order:

service-registry

api-gateway

identity-service

product-service

order-service

payment-service

email-service

📂 Folder Structure
This project is a multi-module Maven project with a clear separation of concerns.

.
├── api-gateway/         # Spring Cloud Gateway
├── common-lib/          # Shared DTOs and entities
├── email-service/       # Handles email notifications
├── identity-service/    # Manages user authentication and authorization
├── order-service/       # Handles order processing and payment logic
├── payment-service/     # Manages payment records
├── product-service/     # Manages the product catalog
├── service-registry/    # Eureka server for service discovery
├── docker-compose.yml   # Infrastructure setup
└── pom.xml              # Root Maven project file

🧑‍💻 Contributors
Gopal - Project Lead & Developer

📄 License
This project is licensed under the MIT License. See the LICENSE file for details.
