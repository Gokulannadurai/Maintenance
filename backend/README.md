# Office Internal Maintenance Web Application

A comprehensive maintenance request management system built with Java 21 (Spring Boot) and React.

## Features

- **User Management**: Role-based access control (Employee, Admin, Super Admin)
- **Maintenance Requests**: Create, track, and manage maintenance requests
- **File Attachments**: Upload and manage files using AWS S3
- **Notifications**: Real-time notifications for status changes
- **Audit Logging**: Complete audit trail for all actions
- **Reporting**: Generate reports based on various criteria

## Technology Stack

### Backend
- **Java**: JDK 21
- **Framework**: Spring Boot 3.2.5
- **Database**: PostgreSQL (Production), H2 (Testing)
- **Security**: Spring Security with JWT
- **File Storage**: AWS S3
- **Build Tool**: Maven

### Frontend
- **Framework**: React (as per requirements)
- **Language**: TypeScript
- **UI Library**: Material-UI or Ant Design

## Quick Start

### Prerequisites
- Java 21
- Maven 3.6+
- PostgreSQL (for production)
- AWS S3 bucket (for file storage)

### Configuration

1. **Copy configuration files**:
   ```bash
   cp src/main/resources/application-dev.yml src/main/resources/application-local.yml
   ```

2. **Update database settings** in `application-local.yml`:
   ```yaml
   spring:
     datasource:
       url: jdbc:postgresql://localhost:5432/maintenance_dev
       username: your_username
       password: your_password
   ```

3. **Update AWS S3 settings**:
   ```yaml
   app:
     aws:
       s3:
         bucket: your-s3-bucket
       access-key-id: your-access-key
       secret-access-key: your-secret-key
       region: us-east-1
   ```

### Running the Application

**Development**:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

**Production**:
```bash
export SPRING_PROFILES_ACTIVE=prod
export DATABASE_URL=...
export AWS_S3_BUCKET=...
java -jar target/maintenanceservice-1.0.0.jar
```

## Project Structure

```
src/main/java/com/ideas2it/maintenanceservice/
├── config/           # Configuration classes
├── controller/       # REST API controllers
├── dto/             # Data Transfer Objects
│   └── mapper/      # DTO-Entity mappers
├── entity/          # JPA entities
├── exception/       # Exception handlers
├── repository/      # Data access layer
├── security/        # Security configuration
├── service/         # Business logic
│   └── impl/        # Service implementations
└── MaintenanceServiceApplication.java
```

## API Documentation

The application provides RESTful APIs for:

- **Authentication**: `/api/auth/*`
- **Users**: `/api/users/*`
- **Maintenance Requests**: `/api/requests/*`
- **Categories**: `/api/categories/*`
- **Locations**: `/api/locations/*`
- **Attachments**: `/api/attachments/*`
- **Notifications**: `/api/notifications/*`
- **Audit Logs**: `/api/audit-logs/*`

Detailed API documentation is available in [API_DOCUMENTATION.md](API_DOCUMENTATION.md).

## Configuration

For detailed configuration information, see [CONFIGURATION.md](CONFIGURATION.md).

### Environment Profiles

- **dev**: Development environment with debug logging
- **prod**: Production environment with optimized settings
- **test**: Test environment with H2 in-memory database

### Key Configuration Properties

- Database connection settings
- AWS S3 credentials and bucket
- JWT secret and expiration
- File upload limits and allowed types
- Logging levels

## Security

- JWT-based authentication
- Role-based authorization
- Input validation and sanitization
- SQL injection prevention (JPA/Hibernate)
- XSS protection
- CORS configuration

## Testing

```bash
# Run all tests
mvn test

# Run with specific profile
mvn test -Dspring.profiles.active=test
```

## Building

```bash
# Build the application
mvn clean package

# Build with tests
mvn clean package -DskipTests=false
```

## Deployment

### Docker (Recommended)

```dockerfile
FROM openjdk:21-jdk-slim
COPY target/maintenanceservice-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

### Traditional Deployment

1. Build the application: `mvn clean package`
2. Set environment variables for production
3. Run: `java -jar target/maintenanceservice-1.0.0.jar`

## Contributing

1. Follow the coding standards defined in [rules.md](rules.md)
2. Write unit tests for new features
3. Ensure 90%+ code coverage
4. Follow the established package structure
5. Use proper logging and error handling

## License

This project is licensed under the MIT License. 