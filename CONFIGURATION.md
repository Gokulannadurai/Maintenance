# Configuration Guide

This document explains how to configure the Maintenance Service application for different environments.

## Environment Configuration Files

The application uses Spring Boot's profile-based configuration:

- `application.yml` - Default configuration
- `application-dev.yml` - Development environment
- `application-prod.yml` - Production environment
- `application-test.yml` - Test environment (embedded in main config)

## Database Configuration

### PostgreSQL Setup

1. **Install PostgreSQL** (if not already installed)
2. **Create Database**:
   ```sql
   CREATE DATABASE maintenance_db;
   CREATE DATABASE maintenance_dev;
   ```

3. **Environment Variables** (Production):
   ```bash
   export DATABASE_URL=jdbc:postgresql://your-db-host:5432/maintenance_db
   export DATABASE_USERNAME=your_db_user
   export DATABASE_PASSWORD=your_db_password
   ```

4. **Local Development**:
   - Use `application-dev.yml` for local development
   - Default: `localhost:5432/maintenance_dev`

### Database Connection Pool (HikariCP)

The application uses HikariCP for connection pooling:

- **Development**: 20 max connections, 5 min idle
- **Production**: 50 max connections, 10 min idle
- **Connection timeout**: 30 seconds
- **Idle timeout**: 10 minutes
- **Max lifetime**: 30 minutes

## AWS S3 Configuration

### S3 Bucket Setup

1. **Create S3 Bucket**:
   ```bash
   aws s3 mb s3://your-maintenance-bucket
   ```

2. **Configure CORS** (if needed):
   ```json
   [
     {
       "AllowedHeaders": ["*"],
       "AllowedMethods": ["GET", "PUT", "POST", "DELETE"],
       "AllowedOrigins": ["*"],
       "ExposeHeaders": []
     }
   ]
   ```

3. **IAM User/Permissions**:
   ```json
   {
     "Version": "2012-10-17",
     "Statement": [
       {
         "Effect": "Allow",
         "Action": [
           "s3:GetObject",
           "s3:PutObject",
           "s3:DeleteObject"
         ],
         "Resource": "arn:aws:s3:::your-maintenance-bucket/*"
       }
     ]
   }
   ```

### Environment Variables

**Production**:
```bash
export AWS_S3_BUCKET=your-maintenance-bucket
export AWS_ACCESS_KEY_ID=your-access-key
export AWS_SECRET_ACCESS_KEY=your-secret-key
export AWS_REGION=us-east-1
```

**Development**:
- Use `application-dev.yml` with mock/local values
- Consider using LocalStack for local S3 testing

## JWT Configuration

### JWT Secret

**Production**:
```bash
export JWT_SECRET=your-super-secure-jwt-secret-key
export JWT_EXPIRATION=86400000  # 24 hours in milliseconds
```

**Development**:
- Use `application-dev.yml` with development secret
- Never use production secrets in development

## File Upload Configuration

### File Size Limits

- **Development**: 5MB
- **Production**: 20MB (configurable)

### Allowed File Types

- Images: `image/*`
- Documents: `application/pdf`, `text/plain`
- Office Documents: `application/msword`, `application/vnd.openxmlformats-officedocument.wordprocessingml.document`

## Running the Application

### Development
```bash
# Using Maven
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Using Java
java -jar target/maintenanceservice-1.0.0.jar --spring.profiles.active=dev
```

### Production
```bash
# Set environment variables first
export SPRING_PROFILES_ACTIVE=prod
export DATABASE_URL=...
export AWS_S3_BUCKET=...

# Run application
java -jar target/maintenanceservice-1.0.0.jar
```

### Testing
```bash
# Tests use H2 in-memory database automatically
mvn test
```

## Configuration Properties Reference

### Database Properties
- `spring.datasource.url` - Database connection URL
- `spring.datasource.username` - Database username
- `spring.datasource.password` - Database password
- `spring.jpa.hibernate.ddl-auto` - Hibernate DDL mode
- `spring.jpa.show-sql` - Show SQL queries in logs

### AWS Properties
- `app.aws.s3.bucket` - S3 bucket name
- `app.aws.access-key-id` - AWS access key
- `app.aws.secret-access-key` - AWS secret key
- `app.aws.region` - AWS region

### JWT Properties
- `app.jwt.secret` - JWT signing secret
- `app.jwt.expiration` - JWT expiration time in milliseconds

### File Upload Properties
- `app.file-upload.max-size` - Maximum file size in bytes
- `app.file-upload.allowed-types` - Comma-separated list of allowed MIME types

## Security Considerations

1. **Never commit secrets** to version control
2. **Use environment variables** for production secrets
3. **Rotate secrets regularly** in production
4. **Use strong JWT secrets** (at least 32 characters)
5. **Limit S3 bucket permissions** to minimum required
6. **Enable database SSL** in production
7. **Use HTTPS** in production

## Troubleshooting

### Database Connection Issues
- Check database is running
- Verify connection URL, username, and password
- Check firewall settings
- Verify database exists

### S3 Upload Issues
- Verify AWS credentials
- Check S3 bucket exists and is accessible
- Verify IAM permissions
- Check network connectivity to AWS

### JWT Issues
- Verify JWT secret is set
- Check JWT expiration time
- Verify clock synchronization 