# 🏦 Banking Application REST API

A secure **Banking REST API** developed using **Java and Spring Boot** for managing customers, accounts, deposits, withdrawals, money transfers, transactions, and mini statements.

The application follows a layered architecture and uses **Spring Security** to secure API endpoints.

## 🚀 Project Overview

This project provides REST APIs for common banking operations while implementing authentication and authorization using Spring Security.

### Core Banking Features

* Customer management
* Bank account management
* Deposit money
* Withdraw money
* Transfer money between accounts
* Transaction management
* Mini statement
* Secure API access

### Security Features

* Spring Security
* Authentication
* Authorization
* Secured REST endpoints
* Password-based authentication
* Role-based access control

---

## 🛠️ Tech Stack

| Technology      | Purpose                        |
| --------------- | ------------------------------ |
| Java 17         | Programming Language           |
| Spring Boot     | Backend Framework              |
| Spring Web MVC  | REST API Development           |
| Spring Security | Authentication & Authorization |
| Spring Data JPA | Database Access                |
| Hibernate       | ORM                            |
| MySQL           | Database                       |
| Lombok          | Reduce Boilerplate Code        |
| Maven           | Build & Dependency Management  |
| Postman         | API Testing                    |

---

## ✨ Features

### 👤 Customer Management

* Create customer
* Retrieve customer information
* Update customer information
* Manage customer-related data

### 💳 Account Management

* Create bank account
* Retrieve account information
* Manage account details
* Check account information

### 💰 Deposit

* Deposit money into an account
* Validate deposit requests
* Update account balance

### 💸 Withdrawal

* Withdraw money from an account
* Validate withdrawal requests
* Update account balance

### 🔄 Money Transfer

* Transfer money between accounts
* Debit amount from sender
* Credit amount to receiver
* Update account balances
* Record transaction details

### 📊 Transaction Management

* Store transaction information
* Retrieve transaction details
* Track banking transactions

### 📄 Mini Statement

* Retrieve recent transactions
* View account transaction history

---

# 🔐 Spring Security

Spring Security is used to protect the Banking REST API and control access to secured endpoints.

### Security Flow

```text
Client
  │
  │  Login / API Request
  ▼
Spring Security
  │
  ├── Authentication
  │
  ├── Authorization
  │
  └── Security Filter Chain
  │
  ▼
Controller
  │
  ▼
Service Layer
  │
  ▼
Repository
  │
  ▼
MySQL
```

### Authentication

The application verifies the user's credentials before allowing access to protected resources.

### Authorization

After authentication, Spring Security determines whether the authenticated user has permission to access a particular endpoint.

---

# 🏗️ Project Architecture

```text
                    Client / Postman
                           │
                           ▼
                  ┌─────────────────┐
                  │ Spring Security │
                  └────────┬────────┘
                           │
                    Authentication
                    & Authorization
                           │
                           ▼
                     Controller
                           │
                           ▼
                       Service
                           │
                           ▼
                      Repository
                           │
                           ▼
                         MySQL
```

### Layer Responsibilities

**Controller**

Handles HTTP requests and exposes REST API endpoints.

**Service**

Contains the application's business logic and banking operations.

**Repository**

Handles database communication using Spring Data JPA.

**Security**

Handles authentication and authorization and protects secured endpoints.

**Model / DTO**

Defines the data structures used for API requests and responses.

---

# 📂 Project Structure

```text
BankingApplicationRestApi
│
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com.javaRestApi.BankingApplicationRestApi
│   │   │
│   │   │   ├── Configure
│   │   │   ├── Controller
│   │   │   │   ├── Account
│   │   │   │   ├── Customer
│   │   │   │   ├── Deposit
│   │   │   │   ├── MiniStatement
│   │   │   │   ├── Transactional
│   │   │   │   ├── Transfer
│   │   │   │   └── Withdrawal
│   │   │   │
│   │   │   ├── Exception
│   │   │   ├── Model
│   │   │   ├── Repository
│   │   │   ├── Security
│   │   │   └── service
│   │   │
│   │   └── resources
│   │       └── application.yaml
│   │
│   └── test
│
├── pom.xml
├── mvnw
├── mvnw.cmd
└── README.md
```

---

# 🧪 API Testing

The REST APIs are tested using **Postman**.

Testing includes:

* GET requests
* POST requests
* PUT requests
* DELETE requests
* Request/response validation
* HTTP status code validation
* Positive test scenarios
* Negative test scenarios
* Authentication testing
* Authorization testing
* Banking transaction validation

---

# ⚙️ How to Run

## 1. Clone Repository

```bash
git clone https://github.com/nitish-2323/BankingApplicationRestApi.git
```

## 2. Configure MySQL

Create a MySQL database and configure your database credentials in:

```text
src/main/resources/application.yaml
```

## 3. Build the Project

```bash
mvn clean install
```

## 4. Run the Application

```bash
mvn spring-boot:run
```

Or run the main Spring Boot application from your IDE.

---

# 🎯 Skills Demonstrated

* Java
* Spring Boot
* REST API Development
* Spring Security
* Authentication
* Authorization
* Spring Data JPA
* Hibernate
* MySQL
* Maven
* DTO-based API design
* Layered Architecture
* CRUD Operations
* Banking Transaction Logic
* Exception Handling
* API Testing with Postman

---

# 📈 Future Improvements

* JWT Authentication
* Role-Based Authorization improvements
* Swagger / OpenAPI documentation
* Unit Testing with JUnit and Mockito
* Integration Testing
* Docker containerization
* CI/CD using GitHub Actions or Jenkins

---

# 👨‍💻 Author

**Nitish Thakur**

GitHub:
https://github.com/nitish-2323
