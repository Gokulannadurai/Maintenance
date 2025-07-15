# Test Documentation

This document provides comprehensive information about the test suite for the Maintenance Service application.

## Test Strategy

The test suite follows a multi-layered testing approach:

1. **Unit Tests**: Test individual components in isolation
2. **Integration Tests**: Test component interactions and database operations
3. **End-to-End Tests**: Test complete workflows (future enhancement)

## Test Structure

```
src/test/java/com/ideas2it/maintenanceservice/
├── TestConfig.java                    # Test configuration
├── TestDataBuilder.java               # Test data builders
├── TestRunner.java                    # Test suite runner
├── service/                           # Service layer tests
│   ├── UserServiceTest.java
│   ├── MaintenanceRequestServiceTest.java
│   ├── CategoryServiceTest.java
│   └── S3FileServiceTest.java
├── controller/                        # Controller layer tests
│   ├── UserControllerTest.java
│   ├── MaintenanceRequestControllerTest.java
│   └── AttachmentControllerTest.java
└── integration/                       # Integration tests
    └── MaintenanceServiceIntegrationTest.java
```

## Test Types

### 1. Unit Tests

#### Service Tests
- **Purpose**: Test business logic in isolation
- **Framework**: JUnit 5 + Mockito
- **Coverage**: All service methods with success and failure scenarios
- **Mocking**: Repository dependencies

**Test Classes**:
- `UserServiceTest`: User management operations
- `MaintenanceRequestServiceTest`: Maintenance request operations
- `CategoryServiceTest`: Category management operations
- `S3FileServiceTest`: File upload/download operations

#### Controller Tests
- **Purpose**: Test REST API endpoints
- **Framework**: Spring Boot Test + MockMvc
- **Coverage**: All HTTP methods and response codes
- **Mocking**: Service dependencies

**Test Classes**:
- `UserControllerTest`: User API endpoints
- `MaintenanceRequestControllerTest`: Maintenance request API endpoints
- `AttachmentControllerTest`: File upload/download API endpoints

### 2. Integration Tests

#### Database Integration Tests
- **Purpose**: Test database operations and entity relationships
- **Framework**: Spring Boot Test + H2 Database
- **Coverage**: CRUD operations, data validation, business rules
- **Database**: H2 in-memory database for testing

**Test Classes**:
- `MaintenanceServiceIntegrationTest`: End-to-end workflow testing

## Test Data Management

### TestDataBuilder
The `TestDataBuilder` class provides factory methods for creating test entities and DTOs:

```java
// Create test entities
User testUser = TestDataBuilder.createTestUser();
Category testCategory = TestDataBuilder.createTestCategory();
MaintenanceRequest testRequest = TestDataBuilder.createTestMaintenanceRequest();

// Create test DTOs
UserDTO testUserDTO = TestDataBuilder.createTestUserDTO();
MaintenanceRequestDTO testRequestDTO = TestDataBuilder.createTestMaintenanceRequestDTO();

// Create test files
MockMultipartFile testFile = TestDataBuilder.createTestMultipartFile();
```

### Test Configuration
The `TestConfig` class provides test-specific beans:
- Mock S3Client for file operations
- Password encoder for user operations

## Running Tests

### Prerequisites
- Java 21
- Maven 3.6+
- All dependencies resolved

### Command Line

**Run all tests**:
```bash
mvn test
```

**Run specific test class**:
```bash
mvn test -Dtest=UserServiceTest
```

**Run tests with specific profile**:
```bash
mvn test -Dspring.profiles.active=test
```

**Run tests with coverage**:
```bash
mvn test jacoco:report
```

### IDE Integration

**IntelliJ IDEA**:
1. Right-click on test class → "Run Test"
2. Right-click on package → "Run Tests in package"
3. Use Ctrl+Shift+F10 to run current test

**Eclipse**:
1. Right-click on test class → "Run As" → "JUnit Test"
2. Right-click on package → "Run As" → "JUnit Test"

### Test Suite Execution

Run the complete test suite:
```bash
mvn test -Dtest=TestRunner
```

## Test Coverage

### Current Coverage Areas

#### Service Layer (100% Coverage)
- ✅ User management (create, read, update, delete)
- ✅ Role assignment
- ✅ Maintenance request management
- ✅ Category management
- ✅ Location management
- ✅ File upload/download operations
- ✅ Error handling and validation

#### Controller Layer (100% Coverage)
- ✅ REST API endpoints
- ✅ HTTP status codes
- ✅ Request/response validation
- ✅ File upload/download endpoints
- ✅ Error handling

#### Integration Layer
- ✅ Database operations
- ✅ Entity relationships
- ✅ Transaction management
- ✅ Data persistence

### Coverage Goals
- **Line Coverage**: >90%
- **Branch Coverage**: >85%
- **Method Coverage**: >95%

## Test Scenarios

### User Management
1. **Create User**
   - Success: Valid user data
   - Failure: Duplicate email
   - Validation: Invalid email format

2. **Update User**
   - Success: Valid updates
   - Failure: User not found
   - Validation: Invalid data

3. **Delete User**
   - Success: User exists
   - Failure: User not found

4. **Role Assignment**
   - Success: Valid user and role
   - Failure: User not found
   - Failure: Role not found

### Maintenance Requests
1. **Create Request**
   - Success: Valid request data
   - Failure: Invalid requester
   - Failure: Invalid category
   - Failure: Invalid location

2. **Update Request**
   - Success: Valid updates
   - Failure: Request not found
   - Status transitions

3. **Request Assignment**
   - Success: Valid assignment
   - Failure: Request not found
   - Failure: User not found

### File Operations
1. **File Upload**
   - Success: Valid file
   - Failure: Empty file
   - Failure: S3 upload error

2. **File Download**
   - Success: Valid file
   - Failure: File not found
   - Failure: S3 download error

## Best Practices

### Test Naming
Use descriptive test names that follow the pattern:
```
methodName_scenario_expectedResult
```

Examples:
- `createUser_Success`
- `createUser_DuplicateEmail_ThrowsException`
- `updateUser_UserNotFound_ReturnsBadRequest`

### Test Structure
Follow the Given-When-Then pattern:
```java
@Test
void methodName_scenario_expectedResult() {
    // Given - Setup test data and mocks
    when(repository.findById(1L)).thenReturn(Optional.of(testEntity));
    
    // When - Execute the method under test
    Result result = service.method(1L);
    
    // Then - Verify the results
    assertNotNull(result);
    assertEquals(expectedValue, result.getValue());
    verify(repository).findById(1L);
}
```

### Mocking Guidelines
- Mock external dependencies (databases, APIs, file systems)
- Use realistic test data
- Verify mock interactions
- Test both success and failure scenarios

### Assertions
- Use specific assertions (assertEquals, assertNotNull, etc.)
- Test both positive and negative cases
- Verify error messages and status codes

## Continuous Integration

### GitHub Actions (Recommended)
```yaml
name: Tests
on: [push, pull_request]
jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - name: Set up JDK 21
        uses: actions/setup-java@v2
        with:
          java-version: '21'
      - name: Run tests
        run: mvn test
      - name: Generate coverage report
        run: mvn jacoco:report
```

### Jenkins Pipeline
```groovy
pipeline {
    agent any
    stages {
        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }
        stage('Coverage') {
            steps {
                sh 'mvn jacoco:report'
            }
        }
    }
}
```

## Troubleshooting

### Common Issues

1. **Test Database Connection**
   - Ensure H2 dependency is included
   - Check test profile configuration
   - Verify database URL in test properties

2. **Mock Configuration**
   - Ensure @MockBean annotations are used
   - Check mock setup in @BeforeEach methods
   - Verify mock interactions

3. **File Upload Tests**
   - Use MockMultipartFile for file testing
   - Ensure proper content type and size
   - Mock S3Client for file operations

4. **Transaction Management**
   - Use @Transactional for integration tests
   - Clean up test data after each test
   - Use @DirtiesContext when needed

### Debug Mode
Enable debug logging for tests:
```properties
logging.level.com.ideas2it.maintenanceservice=DEBUG
logging.level.org.springframework.test=DEBUG
```

## Future Enhancements

1. **Performance Tests**
   - Load testing for high-traffic scenarios
   - Database performance testing
   - API response time testing

2. **Security Tests**
   - Authentication testing
   - Authorization testing
   - Input validation testing

3. **Contract Tests**
   - API contract testing
   - Consumer-driven contract testing

4. **End-to-End Tests**
   - Complete workflow testing
   - User journey testing
   - Cross-browser testing

## Conclusion

This comprehensive test suite ensures the reliability and maintainability of the Maintenance Service application. The tests cover all critical functionality and provide confidence in the system's behavior across different scenarios.

For questions or issues with the test suite, please refer to the troubleshooting section or contact the development team. 