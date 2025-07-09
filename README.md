# Recipes Management Service

## Overview
This repository contains the **Recipes Management Service**, a backend service responsible for managing recipes, products and carts. The service is structured following the **hexagonal architecture** pattern, separating concerns into application, domain, and infrastructure layers.

## API Documentation

Swagger has been configured to document the API endpoints. You can access the API documentation at:

```
http://localhost:8080/swagger-ui.html
```
## Project Structure

### **Application Layer (`application/`)**
The application layer exposes APIs and handles business logic processing.

- **`api/`** - Contains REST APIs:
- **`dto/`** - Data Transfer Objects:
- **`service/`** - Business logic implementation:

### **Domain Layer (`domain/`)**
Contains core business entities and domain logic.

- **`entity/`** - Defines domain objects:
- **`enums/`** - Enum definitions:

### **Infrastructure Layer (`infrastructure/`)**
Handles external integrations, database access, messaging, security, and configuration.

- **`config/`** - Application configurations:
- **`repository/`** - Database repositories:
- **`rest/`** - REST controllers:

## Infrastructure and Dependencies
This project creates the required infrastructure using a **Docker Compose** file, which sets up essential services such as the **database**.

## Setup & Execution
1. Clone the repository.
2. Configure the application properties.
3. Build and run the service using:
   ```sh
   ./gradlew bootRun
   ```
4. Access the REST APIs through configured endpoints.

## License
This project is licensed under the MIT License.

