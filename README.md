# Yushan Analytics Service

> 📊 **Analytics Service for Yushan Platform (Phase 2 - Microservices)** - Tracks user behavior, generates insights, and provides real-time analytics for the gamified web novel reading experience.

## 📋 Overview

Analytics Service is one of the main microservices of Yushan Platform (Phase 2), responsible for collecting and analyzing data. This service manages reading history, rankings, and platform metrics. Uses Redis to cache rankings and scheduled jobs to update rankings periodically.

## Architecture Overview

```
┌─────────────────────────────┐
│   Eureka Service Registry   │
│       localhost:8761        │
└──────────────┬──────────────┘
               │
┌──────────────┴──────────────┐
│   Service Registration &     │
│      Discovery Layer         │
└──────────────┬──────────────┘
               │
    ┌──────────┴──────────┬───────────────┬──────────┬──────────┐
    │                     │               │          │          │
    ▼                     ▼               ▼          ▼          ▼
┌────────┐          ┌─────────┐  ┌────────────┐ ┌──────────┐ ┌──────────┐
│  User  │          │ Content │  │ Engagement │ │Gamifica- │ │Analytics │
│Service │          │ Service │  │  Service   │ │  tion    │ │ Service  │
│ :8081  │◄────────►│  :8082  │  │   :8084    │ │ Service  │ │  :8083   │◄──┐
└────────┘          └─────────┘  └────────────┘ │  :8085   │ └──────────┘   │
    │                     │              │       └──────────┘      │          │
    └─────────────────────┴──────────────┴───────────────────────┘          │
                    Inter-service Communication                               │
                      (via Feign Clients)                                     │
                                                                              │
                    ┌─────────────────────────────────────────────────────────┘
                    │   Analytics Data Collection & Processing
                    ▼
            ┌───────────────┐
            │  Time-Series  │
            │   Database    │
            │  (InfluxDB/   │
            │  PostgreSQL)  │
            └───────────────┘
```

---
## Prerequisites

Before setting up the Analytics Service, ensure you have:
1. **Java 21** installed
2. **Maven 3.8+** or use the included Maven wrapper
3. **Eureka Service Registry** running
4. **PostgreSQL 15+** (for analytics data storage)
5. **Redis** (optional, for caching and real-time metrics)

---
## Step 1: Start Eureka Service Registry

**IMPORTANT**: The Eureka Service Registry must be running before starting any microservice.

```bash
# Clone the service registry repository
git clone https://github.com/phutruonnttn/yushan-microservices-service-registry
cd yushan-microservices-service-registry

# Option 1: Run with Docker (Recommended)
docker-compose up -d

# Option 2: Run locally
./mvnw spring-boot:run
```

### Verify Eureka is Running

- Open: http://localhost:8761
- You should see the Eureka dashboard

---

## Step 2: Clone the Analytics Service Repository

```bash
git clone https://github.com/phutruonnttn/yushan-microservices-analytics-service.git
cd yushan-microservices-analytics-service

# Option 1: Run with Docker (Recommended)
docker-compose up -d

# Option 2: Run locally (requires PostgreSQL 15 to be running beforehand)
./mvnw spring-boot:run
```

---

## Expected Output

### Console Logs (Success)

```
2024-10-16 10:30:15 - Starting AnalyticsServiceApplication
2024-10-16 10:30:18 - Tomcat started on port(s): 8083 (http)
2024-10-16 10:30:20 - DiscoveryClient_ANALYTICS-SERVICE/analytics-service:8083 - registration status: 204
2024-10-16 10:30:20 - Started AnalyticsServiceApplication in 9.2 seconds
```

### Eureka Dashboard

```
Instances currently registered with Eureka:
✅ ANALYTICS-SERVICE - 1 instance(s)
   Instance ID: analytics-service:8083
   Status: UP (1)
```

---

## API Endpoints

### Health Check
- **GET** `/api/v1/health` - Service health status

### Ranking
- **GET** `/api/v1/ranking/novel` - Get novel ranking (with pagination, filters by category, sort by view/vote, time range)
- **GET** `/api/v1/ranking/user` - Get user ranking (with pagination, time range)
- **GET** `/api/v1/ranking/author` - Get author ranking (with pagination, sort by novelNum/view/vote, time range)
- **GET** `/api/v1/ranking/novel/{novelId}/rank` - Get novel's best rank across all categories
- **POST** `/api/v1/ranking/update` - Manually trigger ranking update (ADMIN)

### History (Reading History)
- **POST** `/api/v1/history/novels/{novelId}/chapters/{chapterId}` - Add or update viewing history
- **GET** `/api/v1/history` - Get user's viewing history (with pagination)
- **DELETE** `/api/v1/history/{id}` - Delete a history record
- **DELETE** `/api/v1/history/clear` - Clear all user's history

### Analytics (Admin Only)
- **GET** `/api/v1/admin/analytics/users/trends` - Get user activity trends (with date range, period, filters)
- **GET** `/api/v1/admin/analytics/reading/activity` - Get reading activity trends (with date range, period)
- **GET** `/api/v1/admin/analytics/summary` - Get analytics summary (with date range, period)
- **GET** `/api/v1/admin/analytics/platform/overview` - Get platform-wide statistics overview
- **GET** `/api/v1/admin/analytics/platform/dau` - Get daily active users (with hourly breakdown)
- **GET** `/api/v1/admin/analytics/platform/top-content` - Get top content (novels, authors, categories)

---

## Key Features

### 📊 Ranking System
- Novel rankings (by views or votes, with category filters, time ranges)
- User rankings (by experience points, with time ranges)
- Author rankings (by novel count, views, or votes, with time ranges)
- Best rank tracking for novels
- Manual ranking update (admin)

### 📖 Reading History
- Track user reading progress (novel and chapter)
- View reading history with pagination
- Delete individual history records
- Clear all history

### 📈 Analytics Dashboard (Admin)
- User activity trends analysis
- Reading activity trends
- Comprehensive analytics summary
- Platform-wide statistics overview
- Daily active users (DAU) with hourly breakdown
- Top content analysis (novels, authors, categories)

---

## Database Schema

The Analytics Service uses the following key entities:

- **Ranking** - Ranking data for novels, users, and authors
- **History** - User reading history (novel and chapter tracking)
- **Analytics** - Aggregated analytics data for platform insights

---

## Next Steps

Once this basic setup is working:
1. ✅ Create database entities (UserEvent, ReadingSession, ContentMetrics, etc.)
2. ✅ Set up Flyway migrations for time-series tables
3. ✅ Create repositories and services for data aggregation
4. ✅ Implement API endpoints for analytics queries
5. ✅ Add Feign clients for fetching data from other services
6. ✅ Set up Redis for caching aggregated metrics
7. ✅ Implement scheduled jobs for data aggregation
8. ✅ Add data retention policies
9. ✅ Set up dashboard visualization endpoints

---
## Troubleshooting

**Problem: Service won't register with Eureka**
- Ensure Eureka is running: `docker ps`
- Check logs: Look for "DiscoveryClient" messages
- Verify defaultZone URL is correct

**Problem: Port 8083 already in use**
- Find process: `lsof -i :8083` (Mac/Linux) or `netstat -ano | findstr :8083` (Windows)
- Kill process or change port in application.yml

**Problem: Database connection fails**
- Verify PostgreSQL is running: `docker ps | grep yushan-postgres`
- Check database credentials in application.yml
- Test connection: `psql -h localhost -U yushan_analytics -d yushan_analytics`

**Problem: Build fails**
- Ensure Java 21 is installed: `java -version`
- Check Maven: `./mvnw -version`
- Clean and rebuild: `./mvnw clean install -U`

**Problem: High memory usage**
- Consider implementing data aggregation strategies
- Set up data archiving for old events
- Implement proper indexing on time-series queries
- Configure connection pool sizes appropriately

**Problem: Slow query performance**
- Add database indexes on frequently queried columns
- Implement caching for aggregated metrics
- Consider using database partitioning for large tables
- Use batch processing for event ingestion

---

## Performance Tips
1. **Event Batching**: Use batch endpoints for high-volume event tracking
2. **Caching**: Enable Redis caching for frequently accessed metrics
3. **Data Retention**: Implement policies to archive old data
4. **Indexing**: Ensure proper database indexes on timestamp and user_id columns
5. **Async Processing**: Use asynchronous processing for non-critical analytics

---

## Monitoring
The Analytics Service exposes metrics through:
- Spring Boot Actuator endpoints (`/actuator/metrics`)
- Custom analytics dashboards
- Prometheus-compatible metrics (if configured)

---

## Inter-Service Communication

The Analytics Service communicates with:
- **User Service**: Fetch user profile data
- **Content Service**: Fetch novel and chapter metadata
- **Engagement Service**: Correlate engagement events
- **Gamification Service**: Track achievement-related analytics

### FeignAuthConfig

The service uses `FeignAuthConfig` to forward authentication headers for inter-service calls:

**Priority**:
1. **Gateway-Validated Requests** (Preferred): If incoming request has `X-Gateway-Validated: true`, forward all gateway headers:
   - `X-Gateway-Validated: true`
   - `X-User-Id`, `X-User-Email`, `X-User-Username`, `X-User-Role`, `X-User-Status`
   - `X-Gateway-Timestamp`, `X-Gateway-Signature` (HMAC signature)
2. **Backward Compatibility**: If no gateway headers, forward `Authorization` header (JWT token)

This ensures that:
- Gateway-validated requests maintain their authentication context across services
- HMAC signatures are preserved for security verification
- User status is forwarded to prevent disabled users from accessing resources
- Direct service calls (bypassing Gateway) still work with JWT tokens

---

## 🔐 Authentication & Security

### Authentication Architecture

**JWT Validation is Centralized at API Gateway Level**

- **Primary Flow**: All requests must go through API Gateway which validates JWT tokens
- **Gateway-Validated Requests**: Service trusts requests with `X-Gateway-Validated: true` header
- **HMAC Signature Verification**: Service verifies HMAC-SHA256 signatures to prevent header forgery attacks
- **Backward Compatibility**: Service can still validate JWT tokens directly for inter-service calls

**Filter Chain**:
1. `GatewayAuthenticationFilter` - Processes gateway-validated requests with HMAC signature verification (preferred)
2. `JwtAuthenticationFilter` - Validates JWT tokens (backward compatibility)

**Security Features**:
- **HMAC Signature**: Gateway signs requests with HMAC-SHA256 using shared secret (`GATEWAY_HMAC_SECRET`)
- **Timestamp Validation**: Prevents replay attacks (5-minute tolerance)
- **Constant-Time Comparison**: Prevents timing attacks during signature verification
- **User Status Check**: `GatewayAuthenticationFilter` checks `X-User-Status` header to ensure user is active (`isEnabled()`)
- **Disabled User Rejection**: Disabled/suspended users are rejected with **403 Forbidden** response

### HMAC Configuration

Configure the shared secret for HMAC signature verification in `application.yml`:

```yaml
gateway:
  hmac:
    secret: ${GATEWAY_HMAC_SECRET:yushan-gateway-hmac-secret-key-for-request-signature-2024}
```

**Important**: The same secret must be configured in API Gateway and all microservices.

**Environment Variable**:
- `GATEWAY_HMAC_SECRET`: Shared secret for HMAC signature verification (must match Gateway)

### Security Considerations
- API endpoints should be secured with authentication
- Implement rate limiting for event tracking endpoints
- Validate all incoming event data
- Sanitize user IDs and content IDs in queries
- Use read-only database replicas for reporting queries

---

## 📄 License

This project is part of the Yushan Platform ecosystem.

## 🔗 Links

- **API Gateway**: [yushan-microservices-api-gateway](https://github.com/phutruonnttn/yushan-microservices-api-gateway)
- **Service Registry**: [yushan-microservices-service-registry](https://github.com/phutruonnttn/yushan-microservices-service-registry)
- **Config Server**: [yushan-microservices-config-server](https://github.com/phutruonnttn/yushan-microservices-config-server)
- **Platform Documentation**: [yushan-platform-docs](https://github.com/phutruonnttn/yushan-platform-docs) - Complete documentation for all phases
- **Phase 2 Architecture**: See [Phase 2 Microservices Architecture](https://github.com/phutruonnttn/yushan-platform-docs/blob/main/docs/phase2-microservices/PHASE2_MICROSERVICES_ARCHITECTURE.md)
