# TSS Calendar Backend - Test Plan

## Overview
This document outlines the testing strategy and test cases for the TSS Calendar Backend application.

## Test Categories

### 1. Unit Tests
Location: `src/test/java/com/tsscalendar/TSS/Calendar/`

#### 1.1 Firestore Service Tests (`FirestoreServiceTest.java`)
**Purpose**: Test core Firestore operations and business logic.

**Test Cases**:
- ✅ `testAddEventSuccess()` - Verify event creation with valid parameters
- ✅ `testAddEventWithNullParameters()` - Test error handling for null inputs
- ✅ `testApproveEventSuccess()` - Verify event approval functionality
- ✅ `testApproveEventNotFound()` - Test approval of non-existent event
- ✅ `testDeclineEventSuccess()` - Verify event decline functionality
- ✅ `testGetAllEventsStatusBased()` - Test event retrieval by status
- ✅ `testCheckDocumentExists()` - Test document existence checking
- ✅ `testCredentialLoadingFromEnvironment()` - Test credential loading
- ✅ `testFirebaseInitialization()` - Test Firebase initialization
- ✅ `testEventDataStructure()` - Verify event data structure
- ✅ `testAuditFieldsForApprovedEvent()` - Test audit fields for approval
- ✅ `testAuditFieldsForDeclinedEvent()` - Test audit fields for decline

#### 1.2 Application Context Tests (`TssCalendarApplicationTests.java`)
**Purpose**: Verify Spring Boot application context loads correctly.

**Test Cases**:
- ✅ `contextLoads()` - Basic Spring Boot context loading

### 2. Integration Tests

#### 2.1 Firestore Integration
**Test Cases**:
- **Valid**: Connect to Firebase with valid credentials
- **Valid**: Create, read, update, delete operations
- **Invalid**: Handle connection failures gracefully
- **Invalid**: Handle permission denied scenarios

#### 2.2 Google Forms API Integration
**Test Cases**:
- **Valid**: Fetch form responses with valid form ID
- **Valid**: Handle empty response sets
- **Invalid**: Handle invalid form ID
- **Invalid**: Handle API quota exceeded

#### 2.3 REST Endpoints
**Test Cases**:
- **Valid**: `GET /api/forms/responses` returns JSON data
- **Valid**: `GET /firestore/add` processes successfully
- **Invalid**: Handle malformed requests
- **Invalid**: Handle missing authentication

### 3. Manual Testing Scenarios

#### 3.1 Console Interface Testing
**Test Cases**:
- **Valid**: Navigate all menu options (1-7, 0)
- **Valid**: Fetch events from Google Form (Option 1)
- **Valid**: View pending events (Option 2)
- **Valid**: Approve event by exact title (Option 3)
- **Valid**: Decline event by exact title (Option 4)
- **Valid**: View approved events (Option 5)
- **Valid**: Exit application (Option 0)

**Invalid Input Testing**:
- **Invalid**: Non-numeric menu selection
- **Invalid**: Event title with typos
- **Invalid**: Empty event title
- **Invalid**: Menu option outside range (8, 9, -1)

#### 3.2 Credential Configuration Testing
**Test Cases**:
- **Valid**: Environment variable configuration
- **Valid**: File-based credential configuration
- **Valid**: Spring property configuration
- **Invalid**: Missing credentials
- **Invalid**: Malformed credential JSON

#### 3.3 Logging Verification
**Test Cases**:
- **Valid**: Log file creation at `logs/tss-calendar.log`
- **Valid**: Event operations logged correctly
- **Valid**: Error conditions logged appropriately
- **Valid**: Log rotation and file size management

### 4. Performance Tests

#### 4.1 Load Testing
**Test Cases**:
- **Valid**: Handle 100 concurrent form submissions
- **Valid**: Process large event lists (>1000 events)
- **Valid**: Memory usage under sustained load
- **Valid**: Response time under 2 seconds for API calls

#### 4.2 Stress Testing
**Test Cases**:
- **Valid**: Handle connection timeouts gracefully
- **Valid**: Recover from temporary Firebase outages
- **Valid**: Handle malformed API responses

### 5. Security Tests

#### 5.1 Credential Security
**Test Cases**:
- **Valid**: Credentials not exposed in logs
- **Valid**: Credentials not committed to repository
- **Valid**: Environment variable encryption (if applicable)

#### 5.2 Input Validation
**Test Cases**:
- **Valid**: SQL injection prevention
- **Valid**: XSS prevention in event descriptions
- **Valid**: Path traversal prevention

### 6. Data Integrity Tests

#### 6.1 Event Data Validation
**Test Cases**:
- **Valid**: Required fields validation
- **Valid**: Data type validation
- **Valid**: Field length limits
- **Valid**: Special character handling

#### 6.2 Audit Trail Verification
**Test Cases**:
- **Valid**: `approvedBy` field set on approval
- **Valid**: `approvedAt` timestamp set on approval
- **Valid**: `declinedBy` field set on decline
- **Valid**: `declinedAt` timestamp set on decline
- **Valid**: Timestamp format validation

### 7. Title Normalization Tests

#### 7.1 Case-Insensitive Search
**Test Cases**:
- **Valid**: Find "Test Event" with search "test event"
- **Valid**: Find "TEST EVENT" with search "Test Event"
- **Valid**: Find "TeSt EvEnT" with search "test event"

#### 7.2 Partial Matching
**Test Cases**:
- **Valid**: Handle partial title matches
- **Valid**: Display multiple matches for confirmation
- **Valid**: Handle no matches gracefully

## Test Execution

### Running Unit Tests
```bash
./mvnw test
```

### Running Specific Test Class
```bash
./mvnw test -Dtest=FirestoreServiceTest
```

### Running with Coverage Report
```bash
./mvnw test jacoco:report
```

## Test Data Requirements

### Mock Data for Testing
- **Valid Event**: Complete event data with all required fields
- **Invalid Event**: Missing required fields or invalid data types
- **Edge Cases**: Empty strings, null values, maximum length strings

### Firebase Test Environment
- **Test Project**: Separate Firebase project for testing
- **Test Data**: Isolated test collection
- **Cleanup**: Automated test data cleanup

## Expected Outcomes

### Success Criteria
- All unit tests pass (100% success rate)
- Code coverage > 80% for critical business logic
- All manual test scenarios complete successfully
- Performance benchmarks met
- Security validations pass

### Failure Handling
- Clear error messages for invalid inputs
- Graceful degradation for service unavailability
- Comprehensive logging for debugging
- User-friendly error recovery options

## Test Schedule

### Phase 1: Unit Tests (Completed)
- Firestore service tests
- Application context tests
- Utility function tests

### Phase 2: Integration Tests
- API endpoint testing
- Database integration testing
- External service integration testing

### Phase 3: Manual Testing
- Console interface testing
- End-to-end workflow testing
- User acceptance testing

### Phase 4: Performance & Security Testing
- Load testing
- Security vulnerability scanning
- Performance benchmarking

## Test Environment Setup

### Required Tools
- Java 17+
- Maven 3.6+
- Firebase test project
- Google Forms API access
- Test data sets

### Configuration Files
- `application-test.properties` - Test-specific configuration
- `firebase-test-credentials.json` - Test Firebase credentials
- `test-data.json` - Mock test data

## Bug Reporting

### Bug Report Template
1. **Description**: Clear description of the issue
2. **Steps to Reproduce**: Detailed reproduction steps
3. **Expected Result**: What should have happened
4. **Actual Result**: What actually happened
5. **Environment**: OS, Java version, browser (if applicable)
6. **Logs**: Relevant log entries
7. **Screenshots**: Visual evidence (if applicable)

### Severity Levels
- **Critical**: Application crash, data loss
- **High**: Major functionality broken
- **Medium**: Minor functionality issues
- **Low**: UI/UX improvements, documentation issues

## Test Maintenance

### Regular Updates
- Update test cases for new features
- Maintain test data sets
- Review and update test documentation
- Monitor test execution times

### Continuous Integration
- Automated test execution on code changes
- Test result reporting
- Coverage trend monitoring
- Performance regression detection
