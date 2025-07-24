# 🧾 LocalPOS

**LocalPOS** is an open-source, modular, and offline-ready **Point of Sale (POS)** system tailored for **retail stores and restaurants**. Built for privacy, LocalPOS runs entirely within a **local or private network**, offering robust functionality without requiring internet connectivity or cloud dependencies.

---

## 📦 Project Structure

```bash
localpos/
├── backend/                           
│   ├── java/                         # Java-based microservices
│   │   ├── localpos-user-service/   
│   │   ├── localpos-product-service/ 
│   │   ├── localpos-order-service/  
│   │   ├── localpos-inventory-service/
│   │   └── localpos-reporting-service/
│   └── kotlin/                       # Kotlin-based microservices
│       └── localpos-auth-service-kt/
│
├── frontend/                        # Web and Mobile frontends
│   ├── localpos-web-app/            
│   └── localpos-mobile-app/         
│
├── shared-libraries/                # Shared Java & Kotlin libs
│   ├── java/
│   │   ├── localpos-common/
│   │   └── localpos-common-security/
│   └── kotlin/
│       ├── localpos-common-kt/
│       └── localpos-common-security-kt/
│
├── infrastructure/                 # Docker, NGINX configs
│   ├── docker/                     
│   └── nginx/                      
│
├── docs/                           # Documentation: API specs, diagrams, manuals
│   ├── openapi/                    
│   ├── diagrams/                   
│   └── manuals/                    
│
├── scripts/                        # Utility scripts
└── README.md                       # Project overview
```

---

## 🧩 Modules Overview

| Microservice               | Description                              | Java | Kotlin |
| -------------------------- | ---------------------------------------- | ---- | ------ |
| localpos-auth-service      | Authentication, JWT token management     | ❌    | ✅      |
| localpos-user-service      | User, roles, authentication              | ✅    | 🔜      |
| localpos-product-service   | Product catalog, categories, pricing     | ✅    | 🔜      |
| localpos-order-service     | Sales, transactions, and receipts        | ✅    | 🔜      |
| localpos-inventory-service | Stock management, warehouse, adjustments | ✅    | 🔜      |
| localpos-reporting-service | Reports, summaries, analytics            | ✅    | 🔜      |

---

## 💡 Key Features

- ✅ Offline-first architecture
- 🔐 Local network deployment only — full data ownership
- 🧱 Modular microservices with independent databases
- 📱 Web and mobile frontends
- ☕ Spring Boot in both Java and Kotlin flavors
- 🧪 RESTful APIs with Swagger / OpenAPI
- 🌐 i18n-ready (English and Spanish supported)

---

## 🚀 Getting Started

### Prerequisites

- JDK 21+
- Docker & Docker Compose
- Node.js (for frontend)
- Maven (or use included wrappers)

### Quick Start

```bash
# Start infrastructure (Docker)
cd infrastructure/docker
docker compose up -d

# Start a microservice (Java example)
cd backend/java/localpos-user-service
mvn spring-boot:run

# Start a microservice (Kotlin example)
cd backend/kotlin/localpos-user-service
gradle bootRun
```

---

## 🛠 Technology Stack

- Spring Boot (Java & Kotlin)
- Spring Security, Spring Data JPA
- PostgreSQL per microservice
- Docker & NGINX
- React (TypeScript) / React Native frontends
- Liquibase for database migrations
- JWT for authentication
- Swagger / OpenAPI for API docs
- i18n for multi-language support

---

## 📚 Documentation

All technical and user-facing documentation is available under:

```bash
docs/
├── openapi/
├── diagrams/
└── manuals/
```

---

## 🧪 Testing

Each service includes:

- Unit tests (mocked repositories, services)
- Integration tests (Spring Boot Test)
- Code coverage via JaCoCo or Kover

To run all tests:

```bash
# Java services
mvn test

# Kotlin services
gradle test
```

---

## 📄 License

**LocalPOS** is licensed under the **MIT License**. See `LICENSE` for more details.

---

## 👨‍💻 Maintainers

This project is maintained by the LocalPOS core team. Community contributions are welcome.
