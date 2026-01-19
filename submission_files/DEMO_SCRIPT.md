# TSS Calendar Backend - Demo Script

## Overview
This script provides step-by-step instructions for demonstrating the TSS Calendar Backend functionality.

## Prerequisites
- Java 17+ installed
- Firebase credentials configured
- Maven wrapper available
- Terminal/command line access

## Demo Steps

### 1. Build and Setup (2-3 minutes)

```bash
# Navigate to project directory
cd /path/to/TSS-Calendar

# Build the application
./mvnw clean package

# Verify JAR is created
ls -la target/TSS-Calendar-0.0.1-SNAPSHOT.jar
```

**Expected Output**: 
- Build completes successfully
- JAR file is present in target/ directory (85MB+)

### 2. Configure Credentials (1-2 minutes)

```bash
# Set environment variables (show example)
export FIREBASE_CREDENTIALS_JSON='{"type":"service_account","project_id":"your-project-id",...}'
export FIREBASE_PROJECT_ID="tss-calendar-a03ad"
export SERVER_PORT=8080

# Verify environment variables are set
env | grep FIREBASE
```

**Expected Output**: 
- Environment variables are displayed
- No credential files in repository (verify with `ls -la src/main/resources/`)

### 3. Start Application (30 seconds)

```bash
# Run the application
java -jar target/TSS-Calendar-0.0.1-SNAPSHOT.jar
```

**Expected Output**: 
```
    EVENT MANAGEMENT SYSTEM - BACKEND CONSOLE
    Spring Boot Backend Server Running on Port 8080
════════════════════════════════════════════════════════

[1] Fetch All Events From Google Form
[2] View Pending Events List
[3] Approve Event by Event Title
[4] Decline Event by Event Title
[5] View All Approved Events (Firestore)
[6] Send Email (test)
[7] Activate AI (if available)
[0] Exit

Enter your choice: 
```

### 4. Test Console Operations (2-3 minutes)

#### 4.1 View Pending Events
```bash
# Enter: 2
```

**Expected Output**: 
- List of pending events or "No pending events found"
- Event count displayed

#### 4.2 View Approved Events  
```bash
# Enter: 5
```

**Expected Output**: 
- List of approved events or "No approved events found"
- Event count displayed

#### 4.3 Test Event Approval (if events exist)
```bash
# Enter: 3
# Then: [Exact event title from pending list]
```

**Expected Output**: 
- Success message or "No event found with title: [title]"

### 5. Test REST Endpoints (1-2 minutes)

Open new terminal window:

#### 5.1 Test Google Forms API
```bash
curl http://localhost:8080/api/forms/responses
```

**Expected Output**: 
- JSON response with form data or error message

#### 5.2 Test Firestore Add
```bash
curl http://localhost:8080/firestore/add
```

**Expected Output**: 
```json
{"success":true,"totalAdded":0}
```

### 6. Verify Logging (30 seconds)

```bash
# Check log file
tail -f logs/tss-calendar.log
```

**Expected Output**: 
- Log entries showing application startup
- Event operations (fetch, approve, decline) logged
- Timestamp format: `2026-01-19 09:00:00 [thread] INFO ...`

### 7. Test JavaDoc Generation (30 seconds)

```bash
# Generate documentation
./mvnw javadoc:javadoc

# Verify JavaDoc exists
ls -la target/site/apidocs/index.html
```

**Expected Output**: 
- JavaDoc generated successfully
- HTML documentation created

### 8. Run Unit Tests (30 seconds)

```bash
# Run tests
./mvnw test -Dtest=FirestoreServiceTest
```

**Expected Output**: 
```
Tests run: 12, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

### 9. Clean Exit (30 seconds)

Return to application console:
```bash
# Enter: 0
```

**Expected Output**: 
- Application exits gracefully
- Return to command prompt

## Key Features Demonstrated

### ✅ Build System
- Maven wrapper usage
- Successful JAR compilation
- Dependency management

### ✅ Credential Management
- Environment variable configuration
- No credential files in repository
- Secure credential handling

### ✅ Console Interface
- Interactive menu system
- Event management operations
- User-friendly navigation

### ✅ REST API
- HTTP endpoints accessible
- JSON response format
- Error handling

### ✅ Logging System
- File-based logging
- Operation tracking
- Timestamp formatting

### ✅ Documentation
- Comprehensive JavaDoc
- API documentation generation
- Code comments with @pre/@post tags

### ✅ Testing
- Unit test execution
- Test coverage
- Mock-based testing

### ✅ Data Management
- Firestore integration
- Audit trail implementation
- Title normalization

## Troubleshooting

### Common Issues

1. **Build Failures**
   ```bash
   # Clean and rebuild
   ./mvnw clean compile
   ```

2. **Credential Errors**
   ```bash
   # Verify environment variables
   echo $FIREBASE_CREDENTIALS_JSON | head -c 50
   ```

3. **Port Conflicts**
   ```bash
   # Change port
   export SERVER_PORT=8081
   ```

4. **Log File Issues**
   ```bash
   # Create logs directory
   mkdir -p logs
   ```

## Success Criteria

### ✅ Application starts successfully
### ✅ Console menu is functional
### ✅ REST endpoints respond
### ✅ Logging is active
### ✅ Tests pass
### ✅ JavaDoc generates
### ✅ No credentials in repo
### ✅ Audit fields work
### ✅ Title normalization works

## Total Demo Time: 8-12 minutes

## Final Verification Commands

```bash
# Verify all components
echo "=== Build Verification ==="
ls -la target/TSS-Calendar-0.0.1-SNAPSHOT.jar

echo "=== Documentation Verification ==="
ls -la target/site/apidocs/index.html

echo "=== Test Verification ==="
./mvnw test -q

echo "=== Log Verification ==="
ls -la logs/tss-calendar.log

echo "=== Git Status Verification ==="
git status
```

## Demo Completion

After running this demo script, you will have demonstrated:
- Complete build and deployment process
- All major application features
- Proper security practices
- Comprehensive testing
- Documentation generation
- Logging and monitoring

The application is ready for production use with all required features implemented and tested.
