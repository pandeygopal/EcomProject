# 📘 BuyAnytime - Complete API Documentation

This document provides a comprehensive list of all available API endpoints for the BuyAnytime e-commerce platform. All URLs assume you are making requests through the API Gateway.

**Base URL:** `http://localhost:9191`

---

## 1. Identity Service
Handles user registration, login, and authentication.  
**URL Prefix:** `/api/v1/auth`

| Action                   | Method & URL              | Auth           | Request Body Example                                                                                     | Success Response                    |
|--------------------------|---------------------------|----------------|---------------------------------------------------------------------------------------------------------|-----------------------------------|
| Register a new user       | `POST /register`          | Public         | ```json { "name": "testuser", "email": "test@example.com", "password": "password123", "roles": ["CUSTOMER"] } ``` | `201 CREATED` + Success message   |
| Login and get token       | `POST /token`             | Public         | ```json { "username": "testuser", "password": "password123" } ```                                        | `200 OK` + JWT token              |
| Get current user details  | `GET /me`                 | Bearer Token   | Header: `Authorization: Bearer <token>`                                                                 | `200 OK` + UserDto object         |

---

## 2. Product Service
Manages products, categories, attributes, and variants.  
**URL Prefixes:** `/api/v1/products`, `/api/v1/categories`, `/api/v1/attributes`

### Products

| Action                    | Method & URL                 | Auth               | Request Example                                                                                             | Response                           |
|---------------------------|------------------------------|--------------------|-------------------------------------------------------------------------------------------------------------|------------------------------------|
| Create a new product       | `POST /products`             | Bearer Token (ADMIN/EMPLOYEE) | Form-Data: `name`, `description`, `price`, `stockQuantity`, `multipartFile` (image)                        | `201 CREATED` + ProductResponseDto |
| Get a list of products     | `GET /products`              | Public             | Optional query params: `?page=0&size=10`                                                                    | `200 OK` + Paginated products list |
| Get a single product       | `GET /products/{id}`        | Public             | Path param: `id` (UUID)                                                                                      | `200 OK` + ProductResponseDto      |
| Update a product           | `PUT /products/update/{id}` | Bearer Token (ADMIN/EMPLOYEE) | Header: `If-Match` version number<br>JSON body with fields to update                                         | `200 OK` + Updated ProductResponseDto |
| Delete a product           | `DELETE /products/{id}`     | Bearer Token (ADMIN/EMPLOYEE) | Path param: `id` (UUID)                                                                                      | `200 OK` + Success message         |

### Categories & Attributes

| Action                   | Method & URL             | Auth               | Request Example                                  | Response                       |
|--------------------------|--------------------------|--------------------|-------------------------------------------------|--------------------------------|
| Create a category         | `POST /categories`       | Bearer Token (ADMIN/EMPLOYEE) | `{ "name": "Electronics", "parentId": null }` | `201 CREATED` + CategoryResponseDto |
| Get all categories        | `GET /categories`        | Public             | None                                            | `200 OK` + List of categories   |
| Create an attribute       | `POST /attributes`       | Bearer Token (ADMIN/EMPLOYEE) | `{ "name": "Color", "dataType": "String" }`   | `201 CREATED` + Attribute entity |
| Get all attributes        | `GET /attributes`        | Public             | None                                            | `200 OK` + List of attributes   |

### Product Variants

| Action                   | Method & URL                          | Auth               | Request Example                                                                           | Response                      |
|--------------------------|-------------------------------------|--------------------|-------------------------------------------------------------------------------------------|-------------------------------|
| Create a product variant  | `POST /products/variants/{productId}` | Bearer Token (ADMIN/EMPLOYEE) | `{ "attributes": {"Color": "Black"}, "price": 1349.99, "sku": "LP-BLK-512", "initialStock": 20 }` | `200 OK` + ProductVariantResponseDto |
| Get variants for a product| `GET /products/variants/{productId}` | Public             | Path param: `productId`                                                                    | `200 OK` + List of variants    |

---

## 3. Order Service
Handles checkout, order management, and payment processing.  
**URL Prefix:** `/api/v1/order`

| Action                    | Method & URL                 | Auth          | Request Example                                                                                         | Response                          |
|---------------------------|------------------------------|---------------|--------------------------------------------------------------------------------------------------------|-----------------------------------|
| Create Razorpay Order (Step 1) | `POST /create-payment-order` | Bearer Token | `{ "orderItems": [{"productId": "...", "variantId": 1, "quantity": 1}] }`                              | `200 OK` + Razorpay Order ID      |
| Place Final Order (Step 2) | `POST /`                     | Bearer Token  | Full `OrderRequestDto` with order items, payment method, razorpayOrderId, razorpayPaymentId, signature | `201 CREATED` + OrderDTO          |
| Get order's status         | `GET /{orderId}`             | Bearer Token  | Path param: `orderId`                                                                                   | `200 OK` + OrderResponseDto       |
| Get all orders for user    | `GET /`                      | Bearer Token  | Optional query params: `?page=0&size=10`                                                               | `200 OK` + Paginated orders       |
| Cancel an order            | `POST /cancel/{orderId}`     | Bearer Token  | Path param: `orderId`                                                                                   | `200 OK` + Updated OrderDTO       |

---

## 4. Payment Service
Handles payment information, mostly Kafka-driven.  
**URL Prefix:** `/api/v1/payment`

| Action                  | Method & URL        | Auth          | Request Params / Body     | Response                    |
|-------------------------|---------------------|---------------|--------------------------|-----------------------------|
| Get payment by order ID  | `GET /{orderId}`    | Bearer Token  | Path param: `orderId`    | `200 OK` + PaymentDto        |

---

## Notes
- All authenticated endpoints require the header:  
  `Authorization: Bearer <JWT token>`
- Pagination supported via query params like `?page=0&size=10`
- Use optimistic locking where applicable using `If-Match` header
- Error responses include standard HTTP status codes with error messages

---

## Testing the APIs
- Use **Postman** with a collection exported for your project  
- Swagger UI (if implemented) can be accessed at `/swagger-ui.html` of each service

---

## Contributors
- **Gopal** - Project Lead & Developer

---

## License
This project is licensed under the **MIT License**. See the LICENSE file for details.
