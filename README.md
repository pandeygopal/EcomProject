# 🛍️ BuyAnytime E-Commerce Platform

## 🔍 Description
**BuyAnytime** is a complete, cloud-native e-commerce backend built using a modern microservices architecture. It provides a scalable, resilient, and maintainable foundation for a full-featured online retail platform. The system demonstrates an event-driven ecosystem designed for high performance and independent deployment of its components.

---

## 🚀 Features

- 🔐 **User Authentication & Authorization**  
  Secure registration and login using JWT with role-based access control (Customer, Admin, Employee).

- 📦 **Product Catalog Management**  
  Full CRUD operations for products, categories, and attributes, including image uploads via Cloudinary.

- ⚡ **High-Performance Caching**  
  Redis used for caching product and order data to reduce DB load and improve speed.

- 🔁 **Asynchronous Order Processing**  
  Apache Kafka decouples services; an `OrderEvent` is published, and payment/email services react asynchronously.

- 💳 **Secure Payment Integration**  
  End-to-end payment flow using Razorpay.

- 🌐 **Service Discovery & API Gateway**  
  All services register with Eureka and are routed via Spring Cloud Gateway.

- 🔎 **Distributed Tracing**  
  Zipkin is integrated for full request tracing across microservices.

- 🐳 **Containerized Environment**  
  Entire infrastructure and services are containerized with Docker for consistent deployment.

---

## 🖥️ System Architecture

> 📷
> <img width="3840" height="2368" alt="BuyAnytimeArchitecture" src="https://github.com/user-attachments/assets/19bfe1eb-ad76-470e-a1fb-06065baa6a65" />
Provides a detailed view of all microservices, their core technologies, and interactions.

---

## 🛠️ Tech Stack

| Layer          | Technologies                            |
|----------------|------------------------------------------|
| **Backend**    | Java 17, Spring Boot 3, Spring Cloud     |
| **Database**   | MySQL (Persistence), Redis (Caching)     |
| **Messaging**  | Apache Kafka                             |
| **Auth**       | Spring Security, JWT                     |
| **API Comm**   | REST APIs (OpenFeign)                    |
| **Payments**   | Razorpay SDK                             |
| **Media**      | Cloudinary                               |
| **DevOps**     | Docker, Docker Compose                   |
| **Monitoring** | Zipkin                                   |

---

## 🧪 Installation

### 🔧 Prerequisites
- Java 17 JDK
- Maven 3.8+
- Docker & Docker Compose
- IntelliJ IDEA (or similar IDE)
- Postman (for API testing)

---

### 🚀 Setup Steps

1. **Clone the Repository**
   ```bash
   git clone https://github.com/pandeygopal/EcomProject.git
   cd EcomProject


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

## 📂 Folder Structure

This project is a multi-module Maven project with a clear separation of concerns, organized as follows:

```bash
BuyAnytime/
├── 📂 api-gateway/         # Spring Cloud Gateway for routing and security
├── 📂 common-lib/          # Shared DTOs, entities, and utility classes
├── 📂 email-service/       # Handles asynchronous email notifications
├── 📂 identity-service/    # Manages user authentication and authorization
├── 📂 order-service/       # Handles order processing and payment logic
├── 📂 payment-service/     # Manages payment records from Kafka events
├── 📂 product-service/     # Manages the product catalog and inventory
├── 📂 service-registry/    # Eureka server for service discovery
├── 🐳 docker-compose.yml   # Defines and runs all infrastructure services
└── 📄 pom.xml              # Root Maven project file that manages all modules

🧑‍💻 Contributors
Gopal - Project Lead & Developer

📄 License
This project is licensed under the MIT License. See the LICENSE file for details.
