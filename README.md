# E-commerce Microservices Prototype

This repository contains a minimal microservices architecture for an e‑commerce platform. It demonstrates product catalog, cart and order management, an API gateway, monitoring with Actuator/Prometheus, and a lightweight UI.

## Modules

- `services/user-service`: user authentication, registration, JWT token management
- `services/product-service`: product search, details, inventory
- `services/order-service`: cart operations and order creation
- `gateway`: edge routing to internal services
- `ui`: simple Thymeleaf UI consuming the gateway

## Repository layout

```
services/
  user-service/
  product-service/
  order-service/
gateway/
ui/
pom.xml (parent)
README.md
run-all.ps1
```

## Technology

- Java 21, Spring Boot 3, Spring Cloud Gateway
- Spring Data JPA with in‑memory H2 for persistence
- Spring Security with JWT authentication
- Actuator + Micrometer Prometheus for metrics
- Thymeleaf for server-rendered UI

## Architecture

Request flow: browser → `ui` → `gateway` → domain services (`user-service`, `product-service`, `order-service`). Each service has its own database schema (H2). Gateway centralizes routing. User authentication is handled by `user-service` with JWT tokens. Actuator endpoints expose health and metrics.

## Run locally

```bash
mvn -q -DskipTests package
mvn -q -pl services/user-service -am spring-boot:run &
mvn -q -pl services/product-service -am spring-boot:run &
mvn -q -pl services/order-service spring-boot:run &
mvn -q -pl gateway spring-boot:run &
mvn -q -pl ui spring-boot:run &
```

Open `http://localhost:8085` for the UI. You'll be redirected to the login page.

Health checks: `http://localhost:8083/actuator/health`, `http://localhost:8081/actuator/health`, `http://localhost:8082/actuator/health`, `http://localhost:8080/actuator/health`.

## Sample requests

### Authentication
```bash
# Register a new user
curl -X POST http://localhost:8080/api/auth/signup -H 'Content-Type: application/json' -d '{"name":"John Doe","email":"john@example.com","password":"password123","confirmPassword":"password123"}'

# Login
curl -X POST http://localhost:8080/api/auth/signin -H 'Content-Type: application/json' -d '{"email":"john@example.com","password":"password123"}'
```

### Products
```bash
curl -X POST http://localhost:8081/api/products -H 'Content-Type: application/json' -d '{"sku":"SKU-1","name":"Phone","description":"5G","price":799,"stock":10}'
curl http://localhost:8080/api/products
```

### Cart (requires authentication token)
```bash
# Get token from login response, then use it
curl -X POST http://localhost:8080/api/carts/user-1/items -H 'Content-Type: application/json' -H 'Authorization: Bearer YOUR_JWT_TOKEN' -d '{"productId":1,"quantity":2}'
curl http://localhost:8080/api/carts/user-1 -H 'Authorization: Bearer YOUR_JWT_TOKEN'
```

## Tests

Each module includes unit tests samples (add more as needed) and can be run with `mvn test`.

## Deploy preview

For a quick preview on a single host, run all modules as shown above. For containerized deployment, create Dockerfiles per module and route traffic via the gateway service.
