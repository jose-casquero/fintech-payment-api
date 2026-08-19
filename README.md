# Fintech Payment API - Quality Gates & CI/CD Ready

## Functional Description
The Fintech Payment API is a robust, enterprise-grade RESTful web service designed to simulate a payment processing gateway. Engineered with strict quality gates, it processes payment transactions, validates credit card data, securely masks sensitive information, and tracks the lifecycle of a payment. The project is built using Spring Boot and adheres to a strict layered architecture, ensuring high maintainability and testability. It features automated test coverage enforcement (minimum 85% via JaCoCo) and global exception handling, making it ready for integration into modern CI/CD pipelines.

## Prerequisites
To build and run this project, you will need the following installed on your environment:
- Java Development Kit (JDK) 17 or higher
- Apache Maven 3.8.x or higher
- A REST client (e.g., Postman, cURL) for API testing

## Project Execution
Since this project relies on an in-memory database (H2) and standard Spring Boot configurations, no containerization (Docker) is strictly required for local development.

### Running with Java (Maven)
1. Clone the repository or navigate to the project root directory.
2. Build the project and run the quality gates (Tests + JaCoCo coverage):
   mvn clean verify
3. Start the application:
   mvn spring-boot:run
4. The application will be available at `http://localhost:8080`.
5. Access the H2 Database console at `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:paymentdb`, Username: `sa`, Password: `[blank]`).

## Endpoints Table

| HTTP Method | Endpoint | Description | Request Body | Response Status |
|-------------|----------|-------------|--------------|-----------------|
| POST | `/api/v1/payments` | Creates and processes a new payment. | `PaymentRequest` JSON | `201 Created` |
| GET | `/api/v1/payments/{id}`| Retrieves the details of a specific payment. | None | `200 OK` / `404 Not Found` |

## Usage and Common Scripts

### 1. Create a Payment (Success)
curl -X POST http://localhost:8080/api/v1/payments \
-H "Content-Type: application/json" \
-d '{
    "amount": 150.75,
    "currency": "USD",
    "cardNumber": "1234567890123456"
}'

### 2. Create a Payment (Validation Error - Bad Request)
curl -X POST http://localhost:8080/api/v1/payments \
-H "Content-Type: application/json" \
-d '{
    "amount": -50.00,
    "currency": "US",
    "cardNumber": "1234"
}'

### 3. Retrieve a Payment
curl -X GET http://localhost:8080/api/v1/payments/1

## Architecture
The application follows a classic Enterprise Multi-Layered Architecture:
- **Presentation Layer (Controllers):** Handles HTTP requests, enforces payload validation, and manages HTTP response codes.
- **Business Logic Layer (Services):** Interfaces and implementations (`ServiceImpl`) where the core payment processing rules and transaction management (`@Transactional`) reside.
- **Data Access Layer (Repositories):** Spring Data JPA interfaces for seamless database operations.
- **Data Transfer Objects (DTOs):** Segregated into `Request` and `Response` objects to decouple internal entities from the external API contract.
- **Cross-Cutting Concerns:** Global exception handling (`@RestControllerAdvice`), Data Mapping (custom Mapper), and Utility classes (Card Masking).

## Project Structure
```text
fintech-payment-api
├── pom.xml
├── .gitignore
├── .github
│   └── workflows
│       └── ci.yml
└── src
    ├── main
    │   ├── java
    │   │   └── com
    │   │       └── fintech
    │   │           └── payment
    │   │               ├── PaymentApplication.java
    │   │               ├── controller
    │   │               │   └── PaymentController.java
    │   │               ├── entity
    │   │               │   └── PaymentEntity.java
    │   │               ├── exception
    │   │               │   └── GlobalExceptionHandler.java
    │   │               ├── mapper
    │   │               │   └── PaymentMapper.java
    │   │               ├── repository
    │   │               │   └── PaymentRepository.java
    │   │               ├── request
    │   │               │   └── PaymentRequest.java
    │   │               ├── response
    │   │               │   └── PaymentResponse.java
    │   │               ├── service
    │   │               │   ├── PaymentService.java
    │   │               │   └── impl
    │   │               │       └── PaymentServiceImpl.java
    │   │               └── util
    │   │                   └── CardUtil.java
    │   └── resources
    │       └── application.properties
    └── test
        └── java
            └── com
                └── fintech
                    └── payment
                        └── service
                            └── impl
                                └── PaymentServiceImplTest.java


```                                