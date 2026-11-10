# Fint — Enterprise Financial Ledger & Real-Time Analytics Platform

[![CI/CD Pipeline](https://img.shields.io/badge/CI%2FCD-Passing-brightgreen)](#)
[![Java](https://img.shields.io/badge/Java-17-orange)](#)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-6DB33F)](#)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-336791)](#)
[![License](https://img.shields.io/badge/License-MIT-blue)](#)

**Author:** Bhavishya Pande  
**Repository:** [https://github.com/soham2yu/Fint.git](https://github.com/soham2yu/Fint.git)

---

## Overview
**Fint** is a production-ready, full-stack financial ledger, budget tracking, and transaction processing system built as a Capstone Engineering Project. It implements strict **ACID transaction isolation**, **JPA Optimistic Locking (`@Version`)** to prevent concurrent balance race conditions, **JWT Role-Based Access Control (RBAC)**, and a dark-mode analytical dashboard with navy/aqua/teal visual aesthetics.

## System Architecture
- **Backend API:** Java 17, Spring Boot 3.2, Spring Data JPA, Spring Security (JWT), HikariCP
- **Database & Cache:** PostgreSQL 16 (with composite B-Tree indexing), Redis 7
- **Frontend UI:** Responsive Dark-Mode Analytical Dashboard (HTML5, Tailwind CSS, Chart.js, REST Client)
- **DevOps & QA:** Docker & Docker Compose, GitHub Actions CI/CD, JUnit 5, Mockito, Testcontainers

## Quick Start (Docker Compose)
```bash
git clone https://github.com/soham2yu/Fint.git
cd Fint
docker-compose up -d --build
```
- **Frontend Dashboard:** `http://localhost:3000`
- **Backend REST API & Swagger UI:** `http://localhost:8080/swagger-ui.html`
- **Health Actuator:** `http://localhost:8080/actuator/health`

## Running Automated Tests
```bash
cd backend
./mvnw clean test
```

## API Endpoints Summary
| Method | Endpoint | Description | Auth |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/v1/auth/login` | Authenticate user & issue signed JWT | Public |
| `GET` | `/api/v1/accounts/{accountNumber}` | Fetch real-time balance & metadata | Bearer JWT |
| `POST` | `/api/v1/transactions` | Execute atomic DEBIT/CREDIT transaction | Bearer JWT |
| `GET` | `/api/v1/transactions/audit` | Export paginated ledger history | Bearer JWT |
