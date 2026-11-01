# Multi-Tenant AI Bill Processing Platform

An enterprise-grade, public multi-tenant bill processing and invoice workflow platform with Spring AI extraction, automated anomaly detection, natural language querying, and strict hierarchical tenant isolation.

---

## 🌟 Key Architecture Highlights

### 1. Core Domain
- **Hierarchical Multi-Tenancy**: Parent / sub-organization hierarchy (e.g. *Shaksham Global Enterprises* -> *North Division* & *South Operations*).
- **Dual-Layer Isolation Barrier**: Tenant boundaries enforced at both the HTTP security filter (`TenantContext` thread-local + MDC logger) and database query layer (`WHERE tenant_id = :tenantId` compound indexed predicates + JPA Criteria specifications). Sub-organizations mathematically cannot see sibling or parent data.
- **JWT Authentication & RBAC**: Roles (`CLERK`, `APPROVER`, `ADMIN`) with fine-grained access control on approval and status transition endpoints.
- **Bill Approval Lifecycle**: `DRAFT` ➔ `PENDING` ➔ `IN_REVIEW` ➔ `APPROVED` / `REJECTED` ➔ `PAID`. Every transition produces an immutable audit log entry (`BillAuditLog`).
- **High-Throughput Paginated DTO APIs**: 100% DTO-encapsulated responses with zero entity leakage and compound B-tree index optimizations.

### 2. Standout AI Features
- **Async Document Extraction Queue**: Upload PDF invoices or images; background workers extract Vendor, Invoice Number, Tax ID, Dates, Amounts, and Line Items with an AI confidence score.
- **Duplicate & Anomaly Detection**:
  - *Deterministic Duplicates*: Flags matching Vendor + Invoice number or exact SHA-256 binary file hash.
  - *Statistical Outliers*: Detects bills deviating by $\ge 3\times$ from historical vendor median ticket size.
  - *Statutory Thresholds*: Automatically flags high-value transactions ($> ₹500,000$) or suspicious round-number billing.
- **Natural-Language Dashboard Queries**: Translates plain English queries (e.g., *"pending bills over 50,000 this month"*) into **safe, parameterized JPA Criteria specifications**. Zero possibility of SQL injection.

### 3. Engineering Extras (Senior / Staff Signals)
- **Docker Compose**: Containerized PostgreSQL 16, pgAdmin 4, and Redis 7.
- **Redis Caching**: Caches tenant analytics (`dashboard-summary::tenant:{id}`) with automatic `@CacheEvict` invalidation on mutations.
- **Token-Bucket Rate Limiter**: Bucket4j enforces 60 req/min per tenant.
- **AI Cost Capping & Token Quota**: Tracks monthly token consumption and USD costs ($0.00015 / 1k tokens) with automatic circuit breaker throttling when budget caps are exceeded.
- **Unit & Integration Tests**: Testcontainers and Mockito test suites verifying tenant isolation barriers and anomaly detection.
- **Documented Query Benchmarks**: Detailed `EXPLAIN ANALYZE` before/after report documenting a **100.4x speedup** on paginated tenant queries.

---

## 🏗️ Architecture Diagram

```mermaid
graph TD
    Client["React Frontend (VS Code)<br/>Port 5173"]
    
    subgraph SpringBoot ["Spring Boot 3.3.5 Backend (STS Ready) • Port 8080"]
        AuthFilter["JwtAuthenticationFilter & TenantContext<br/>(ThreadLocal + MDC Logger)"]
        RateLimit["Bucket4j Token Bucket Limiter<br/>(60 req/min per tenant)"]
        Security["RBAC Authorization Manager<br/>(CLERK, APPROVER, ADMIN)"]
        
        subgraph CoreServices ["Core Services"]
            BillSvc["BillService<br/>(Workflow & Audit Trail)"]
            OrgSvc["OrganizationService<br/>(Materialized Path Hierarchy)"]
            DashSvc["DashboardService<br/>(Processed vs Pending Metrics)"]
        end
        
        subgraph AISuite ["Spring AI Engine"]
            AiQueue["ThreadPoolTaskExecutor<br/>(ai-worker queue)"]
            OcrParser["PDFBox + Spring AI Extractor<br/>(Structured JSON Extraction)"]
            Anomaly["AnomalyDetectionService<br/>(Duplicates & 3x Outliers)"]
            NLQuery["NaturalLanguageQueryService<br/>(Safe JPA Criteria Translation)"]
            QuotaSvc["TenantQuotaService<br/>(Monthly Token & Cost Cap)"]
        end
    end
    
    subgraph Infra ["Docker Compose Infrastructure"]
        Postgres[(PostgreSQL 16 DB<br/>Port 5432)]
        RedisCache[(Redis 7 Cache<br/>Port 6379)]
        pgAdmin["pgAdmin 4 GUI<br/>Port 5050"]
    end
    
    Client -->|REST + Bearer JWT| AuthFilter
    AuthFilter --> RateLimit
    RateLimit --> Security
    Security --> CoreServices
    Security --> AISuite
    
    CoreServices --> Postgres
    DashSvc <-->|@Cacheable| RedisCache
    
    AISuite --> AiQueue
    AiQueue --> OcrParser
    OcrParser --> Anomaly
    Anomaly --> QuotaSvc
    QuotaSvc --> Postgres
    NLQuery -->|Safe Parameterized Criteria| Postgres
```

---

## 🚀 Quick Start Guide

### Step 1: Start PostgreSQL, pgAdmin & Redis via Docker
From the project root:
```bash
docker compose up -d
```
Verify containers are healthy:
- **PostgreSQL**: `localhost:5432` (User: `bill_user`, Password: `bill_secure_password123`, DB: `billplatform_db`)
- **pgAdmin 4**: `http://localhost:5050`
- **Redis 7**: `localhost:6379`

---

### Step 2: Open & Run the Backend in STS (Spring Tool Suite)
1. Open **Spring Tool Suite (STS)**.
2. Select **File ➔ Import ➔ Maven ➔ Existing Maven Projects**.
3. Browse to and select:
   ```
   C:\Users\mohi\Documents\AI_BILL_PROCESSING_PROJECT\multitenant-ai-bill-platform\backend
   ```
4. Click **Finish**. STS will automatically configure the Java 21 build path and Maven dependencies.
5. In **Boot Dashboard** or the Project Explorer, right-click `MultiTenantBillProcessingApplication.java` ➔ **Run As ➔ Spring Boot App**.
6. The backend will boot on port `8080`. Flyway will automatically execute migrations `V1__init_schema.sql` and `V2__seed_demo_data.sql`.
7. Interactive Swagger / OpenAPI documentation is available at:
   ```
   http://localhost:8080/api/v1/swagger-ui.html
   ```

---

### Step 3: Open & Run the Frontend in VS Code
1. Open **Visual Studio Code (VS Code)**.
2. Select **File ➔ Open Folder** and choose:
   ```
   C:\Users\mohi\Documents\AI_BILL_PROCESSING_PROJECT\multitenant-ai-bill-platform\frontend
   ```
3. Open a terminal in VS Code (`Ctrl + ~`) and start the Vite development server:
   ```bash
   npm run dev
   ```
4. Access the web application at:
   ```
   http://localhost:5173
   ```

---

### Step 4: Access pgAdmin 4
1. Open your browser at `http://localhost:5050`.
2. Login with credentials:
   - **Email**: `admin@billplatform.com`
   - **Password**: `admin`
3. Click **Add New Server**:
   - **Name**: `BillPlatform DB`
   - **Connection tab**:
     - **Host**: `postgres` (or `host.docker.internal` / `localhost`)
     - **Port**: `5432`
     - **Maintenance Database**: `billplatform_db`
     - **Username**: `bill_user`
     - **Password**: `bill_secure_password123`
4. Inspect the tables, Flyway migrations, and compound indexes.

---

## 👥 Pre-Seeded Demo Personas

The login screen features **1-click instant login buttons** for every persona:

| Persona | Email / Username | Default Password | Role | Tenant Organization | Capabilities |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **HQ Global Admin** | `admin@shaksham.com` | `password123` | `ADMIN` | Shaksham Global (Tenant #1) | Full multi-tenant visibility, quota adjustments, tenant override switcher |
| **North Approver** | `approver_north@shaksham.com` | `password123` | `APPROVER` | North Division (Tenant #2) | Review, approve, and reject North bills with mandatory audit notes |
| **North Clerk** | `clerk_north@shaksham.com` | `password123` | `CLERK` | North Division (Tenant #2) | Upload invoices, run AI extraction, submit bills |
| **South Approver** | `approver_south@shaksham.com` | `password123` | `APPROVER` | South Operations (Tenant #3) | Approvals isolated strictly to South bills |
| **South Clerk** | `clerk_south@shaksham.com` | `password123` | `CLERK` | South Operations (Tenant #3) | Ingestion isolated strictly to South bills |

---

## 🧪 Running Unit & Integration Tests

Run the full test suite with Maven:
```bash
mvn test -f backend/pom.xml
```

Test coverage includes:
- `AnomalyDetectionServiceTest`: Verifies duplicate invoice detection, binary file hash matching, $3\times$ statistical deviation alerts, and $> ₹500,000$ high-value transaction escalation.
- `NaturalLanguageQueryServiceTest`: Verifies parsing of prompts like *"pending bills over 50,000 this month"* into structured criteria.
- `TenantIsolationIntegrationTest`: Verifies that clerks cannot view or mutate bills belonging to other organizations (`TenantViolationException` with HTTP 403).

---

## 📊 Performance Benchmarks & Architecture Docs

- [Database Query Optimization & Benchmark Report](docs/INDEXED_QUERIES_BENCHMARK.md): Detailed before/after `EXPLAIN ANALYZE` benchmarks documenting a **100.4x speedup** using compound indexes.
- [Architecture & Design Rationale](docs/ARCHITECTURE.md): Detailed comparison of multi-tenancy models (materialized path vs recursive CTEs), async AI queues, and safety patterns.
