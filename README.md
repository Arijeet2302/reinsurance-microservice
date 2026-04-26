# Reinsurance Microservice

A modern Java-based microservice built with Spring Boot 3.2.0 and Java 21 for syncing insurance policy data from a Policy Admin System and managing reinsurance transactions.

## 🎯 Features

### Policy Synchronization
- ✅ Sync single policies from Policy Admin System
- ✅ Batch sync multiple policies
- ✅ Automatic reinsurance premium calculation
- ✅ Track sync source and timestamp
- ✅ Query policies by status, provider, and sync time
- ✅ Support for policy status management (ACTIVE, INACTIVE, EXPIRED, PENDING)

### Transaction Management
- ✅ Create and track reinsurance transactions
- ✅ Multiple transaction types: PREMIUM_PAYMENT, CLAIM_SETTLEMENT, ADJUSTMENT, REFUND
- ✅ Transaction approval workflow
- ✅ Transaction status tracking: PENDING, COMPLETED, FAILED, REJECTED
- ✅ Date range filtering and analytics
- ✅ Pending approval queries

### Production Ready
- ✅ Comprehensive input validation
- ✅ Global exception handling
- ✅ Optimistic locking with versioning
- ✅ Database indexing for performance
- ✅ Detailed logging
- ✅ H2 (development) and PostgreSQL (production) support
- ✅ API response wrapper for consistency

## 🛠️ Technology Stack

- **Java**: 21
- **Spring Boot**: 3.2.0
- **Spring Data JPA**: For database operations
- **PostgreSQL/H2**: Database (PostgreSQL for production, H2 for development)
- **Lombok**: Code generation
- **Jakarta Validation**: Input validation
- **SLF4J & Logback**: Logging

## 📋 Prerequisites

- Java 21 or higher
- Maven 3.6+
- PostgreSQL 12+ (for production)

## 🚀 Quick Start

### Build the Application

```bash
mvn clean install
```

### Run the Application

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8081`

### Access H2 Console (Development)

```
http://localhost:8081/h2-console
```

## 📚 API Endpoints

### Policy Sync Endpoints

#### 1. Sync Single Policy
```http
POST /api/v1/policies/sync
Content-Type: application/json

{
  "policy_number": "POL-2024-001",
  "customer_name": "John Doe",
  "customer_email": "john@example.com",
  "policy_type": "HEALTH",
  "premium_amount": 50000.00,
  "reinsurance_percentage": 25.00,
  "policy_start_date": "2024-01-01T00:00:00",
  "policy_end_date": "2025-01-01T00:00:00",
  "provider_name": "ACME Insurance",
  "external_policy_id": "EXT-001"
}
```

#### 2. Batch Sync Policies
```http
POST /api/v1/policies/sync/batch
Content-Type: application/json

{
  "policies": [
    { ... policy1 ... },
    { ... policy2 ... }
  ]
}
```

#### 3. Get All Policies
```http
GET /api/v1/policies
```

#### 4. Get Policy by ID
```http
GET /api/v1/policies/{id}
```

#### 5. Get Policies by Status
```http
GET /api/v1/policies/status/{status}
```
Valid statuses: `ACTIVE`, `INACTIVE`, `EXPIRED`, `PENDING`

#### 6. Get Policies by Provider
```http
GET /api/v1/policies/provider/{providerName}
```

#### 7. Get Policies Synced After Time
```http
GET /api/v1/policies/synced-after?timestamp=2024-01-01T00:00:00
```

#### 8. Update Policy Status
```http
PATCH /api/v1/policies/{id}/status?status=INACTIVE
```

#### 9. Policy Health Check
```http
GET /api/v1/policies/health
```

### Transaction Endpoints

#### 1. Create Transaction
```http
POST /api/v1/transactions
Content-Type: application/json

{
  "policy_id": 1,
  "transaction_type": "PREMIUM_PAYMENT",
  "transaction_amount": 12500.00,
  "transaction_date": "2024-01-15T10:00:00",
  "description": "First premium payment",
  "payment_method": "BANK_TRANSFER",
  "reference_document_id": "INV-001"
}
```

#### 2. Get All Transactions
```http
GET /api/v1/transactions
```

#### 3. Get Transaction by ID
```http
GET /api/v1/transactions/{id}
```

#### 4. Get Transactions by Policy
```http
GET /api/v1/transactions/policy/{policyId}
```

#### 5. Get Transactions by Status
```http
GET /api/v1/transactions/status/{status}
```
Valid statuses: `PENDING`, `COMPLETED`, `FAILED`, `REJECTED`

#### 6. Get Transactions by Type
```http
GET /api/v1/transactions/type/{type}
```
Valid types: `PREMIUM_PAYMENT`, `CLAIM_SETTLEMENT`, `ADJUSTMENT`, `REFUND`

#### 7. Get Transactions by Date Range
```http
GET /api/v1/transactions/date-range?startDate=2024-01-01T00:00:00&endDate=2024-12-31T23:59:59
```

#### 8. Approve Transaction
```http
PATCH /api/v1/transactions/{id}/approve?approvedBy=John Smith
```

#### 9. Reject Transaction
```http
PATCH /api/v1/transactions/{id}/reject?approvedBy=John Smith
```

#### 10. Get Pending Approvals
```http
GET /api/v1/transactions/approvals/pending
```

#### 11. Get Transaction Analytics
```http
GET /api/v1/transactions/analytics?startDate=2024-01-01T00:00:00&endDate=2024-12-31T23:59:59
```

#### 12. Transaction Health Check
```http
GET /api/v1/transactions/health
```

## 📊 API Response Format

All API responses follow a consistent format:

```json
{
  "success": true,
  "message": "Operation successful",
  "data": { ... },
  "timestamp": 1704067200000
}
```

## 🗄️ Database Schema

### insurance_policies Table
- Indexes on: policy_number, status, sync_source
- Supports optimistic locking with version field
- Tracks creation and update timestamps

### reinsurance_transactions Table
- Indexes on: transaction_reference, policy_id, status, transaction_date
- Supports optimistic locking with version field
- Tracks approval workflow

## 🔐 Validation Rules

### Policy Request
- Policy number: 3-50 characters, unique
- Customer name: 2-100 characters
- Email: Valid email format
- Premium amount: > 0
- Reinsurance percentage: 0-100

### Transaction Request
- Transaction amount: > 0
- Description: Max 500 characters
- Policy must exist

## 📝 Configuration

Edit `src/main/resources/application.properties` to configure:

```properties
# Server
server.port=8081

# Database (H2 for development)
spring.datasource.url=jdbc:h2:mem:reinsurance_db
spring.h2.console.enabled=true

# Logging
logging.level.com.insurance.reinsurance=DEBUG
```

### Production Configuration (PostgreSQL)

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/reinsurance_db
spring.datasource.username=postgres
spring.datasource.password=password
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=validate
```

## 🧪 Testing

Run tests with:

```bash
mvn test
```

## 📦 Project Structure

```
reinsurance-microservice/
├── pom.xml
├── README.md
└── src/
    └── main/
        ├── java/com/insurance/reinsurance/
        │   ├── ReinsuranceMicroserviceApplication.java
        │   ├── controller/
        │   │   ├── PolicySyncController.java
        │   │   └── TransactionController.java
        │   ├── service/
        │   │   ├── PolicySyncService.java
        │   │   └── TransactionService.java
        │   ├── repository/
        │   │   ├── InsurancePolicyRepository.java
        │   │   └── ReinsuranceTransactionRepository.java
        │   ├── entity/
        │   │   ├── InsurancePolicy.java
        │   │   └── ReinsuranceTransaction.java
        │   ├── dto/
        │   │   ├── InsurancePolicyRequest.java
        │   │   ├── InsurancePolicyResponse.java
        │   │   ├── ReinsuranceTransactionRequest.java
        │   │   ├── ReinsuranceTransactionResponse.java
        │   │   ├── BatchPolicySyncRequest.java
        │   │   └── ApiResponse.java
        │   └── exception/
        │       ├── ResourceNotFoundException.java
        │       └── GlobalExceptionHandler.java
        └── resources/
            └── application.properties
```

## 🚀 Deployment

### Build Docker Image

```bash
mvn clean package
docker build -t reinsurance-microservice:1.0.0 .
```

### Run Docker Container

```bash
docker run -p 8081:8081 reinsurance-microservice:1.0.0
```

## 📄 License

MIT License

## 👨‍💻 Author

Arijeet - Insurance Reinsurance Systems

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## ❓ FAQ

**Q: How is reinsurance premium calculated?**
A: Reinsurance Premium = Premium Amount × (Reinsurance Percentage / 100)

**Q: What happens when I sync a duplicate policy?**
A: The system will reject it and return an error. Ensure policy numbers are unique.

**Q: Can I update policy details after creation?**
A: Currently, the service allows status updates. Full CRUD operations can be added as needed.

**Q: What is the transaction approval workflow?**
A: Transactions are created in PENDING status. They must be approved (changing to COMPLETED) or rejected by an authorized user.
