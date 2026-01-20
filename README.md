# TSS Calendar Backend

A comprehensive Spring Boot application for event management that integrates with Google Forms, Firebase Firestore, and provides robust admin controls with built-in validation and testing capabilities.

## Overview

This backend system:
- Fetches event submissions from Google Forms using the Google Forms API
- Stores and manages events in Firebase Firestore with approval workflow
- Provides interactive console-based admin interface with comprehensive validation
- Exposes REST endpoints for external system integration
- Features built-in system integrity checks and comprehensive test suites
- Includes email notification capabilities via Resend API
- Uses Java 17+, Spring Boot 4.0.0 with modern dependency management

## Requirements

- Java 17 or later
- Maven 3.6+ (or use included Maven wrapper)
- Internet access for Google APIs & Firestore
- Firebase service account JSON for Firestore access
- Google credentials JSON for Google Forms API

## Setup & Run

### 1. Build the application
```bash
# Using Maven wrapper (recommended)
./mvnw clean package

# Or using system Maven
mvn clean package
```

### 2. Configure credentials

**For Production (Recommended):**
Set environment variables:

```bash
# Firebase credentials (required)
export FIREBASE_CREDENTIALS_JSON="$(cat /path/to/firebase-credential.json)"
export FIREBASE_PROJECT_ID="tss-calendar-a03ad"

# Optional: Server port
export SERVER_PORT=8080
```

**For Local Development:**
Place credential files in `src/main/resources/`:
- `firebase-credential.json` (Firebase service account key)
- `credential.json` (Google Forms API service account key)

> **IMPORTANT**: These files are gitignored and will not be committed to version control.

**Docker/Container Environment:**
```bash
docker run -e FIREBASE_CREDENTIALS_JSON="$(cat firebase-credential.json)" \
           -e FIREBASE_PROJECT_ID="tss-calendar-a03ad" \
           -p 8080:8080 \
           tss-calendar-backend
```

### 3. Run the application
```bash
java -jar target/TSS-Calendar-0.0.1-SNAPSHOT.jar
```

## Console Usage

The application provides an interactive console menu with comprehensive validation:

```
════════════════════════════════════════════════════════
    EVENT MANAGEMENT SYSTEM - BACKEND CONSOLE
    Spring Boot Backend Server Running on Port 8080
════════════════════════════════════════════════════════

[1] Fetch All Events From Google Form
[2] View Pending Events List
[3] Approve Event by Event Title
[4] Decline Event by Event Title
[5] View All Approved Events (Firestore)
[6] Send Email
[7] Run System Validation Tests
[0] Exit
```

**Menu Options:**
- **Option 1**: Fetches Google Forms responses and adds new events to Firestore
- **Option 2**: Displays all pending events with detailed information
- **Option 3**: Approves an event by exact title (with input validation)
- **Option 4**: Declines an event by exact title (with input validation)
- **Option 5**: Shows all approved events with comprehensive details
- **Option 6**: Access email notification system
- **Option 7**: Program shutdown

**Built-in Validation Features:**
- Input validation for all user interactions
- System integrity checks on startup
- Comprehensive test suite for valid/invalid data scenarios
- Edge case testing and error handling
- Real-time validation feedback

## REST Endpoints

- `GET /api/forms/responses` - Returns Google Forms responses
- `GET /firestore/add` - Fetches responses and adds them to Firestore

Example:
```bash
curl http://localhost:8080/api/forms/responses
curl http://localhost:8080/firestore/add
```

## Logging

The application logs events to `logs/tss-calendar.log`. Configure in `application.properties`:

```properties
logging.file.name=logs/tss-calendar.log
logging.level.root=INFO
logging.level.com.tsscalendar=DEBUG
```

**Log Location**: `logs/tss-calendar.log` (automatically created in project root)

## Testing

Run unit tests:
```bash
./mvnw test
```

## JavaDoc

Generate API documentation:
```bash
./mvnw javadoc:javadoc
```
**JavaDoc Location**: `target/site/apidocs/`

## Runnable JAR

**JAR Location**: `target/TSS-Calendar-0.0.1-SNAPSHOT.jar`

To run the application:
```bash
java -jar target/TSS-Calendar-0.0.1-SNAPSHOT.jar
```

The JAR includes all dependencies and can be run on any system with Java 17+ installed.

## Project Structure

```
src/
├── main/
│   ├── java/com/tsscalendar/TSS/Calendar/
│   │   ├── TssCalendarApplication.java (Main application with console interface)
│   │   ├── controller/
│   │   │   ├── FirestoreController.java (REST endpoints)
│   │   │   └── GoogleFormController.java (Google Forms integration)
│   │   ├── service/
│   │   │   ├── Firestore.java (Firestore operations)
│   │   │   ├── GoogleForm.java (Google Forms API client)
│   │   │   └── AiClient.java (AI integration)
│   │   └── EmailSend.java (Email notifications)
│   └── resources/
│       ├── application.properties
│       ├── credential.json (gitignored - Google Forms API)
│       ├── firebase-credential.json (gitignored - Firebase)
│       └── static/
└── test/
    └── java/
        └── TssCalendarApplicationTests.java
```

## Key Features

### Event Management Workflow
- **Submission**: Events submitted via Google Forms
- **Storage**: Automatically stored in Firestore with `pending` status
- **Approval**: Admin approval via console interface
- **Tracking**: Full audit trail with timestamps and user actions

### Validation & Testing
- **System Integrity**: Automatic validation on startup
- **Input Validation**: Real-time validation for all user inputs
- **Test Suite**: Comprehensive validation tests (Option 7)
- **Error Handling**: Robust error handling with user-friendly messages

### Audit Trail
- `approvedBy` - User who approved the event
- `approvedAt` - Timestamp when approved
- `declinedBy` - User who declined the event  
- `declinedAt` - Timestamp when declined
- `title_lower` - Normalized title for case-insensitive search

## Dependencies

- **Spring Boot 4.0.0** - Main application framework
- **Firebase Admin SDK 9.7.0** - Firestore database operations
- **Google Forms API v1-rev20250422-2.0.0** - Google Forms integration
- **Google Drive API v3-rev20240914-2.0.0** - Google Drive access
- **Google API Client 2.7.2** - Google API client libraries
- **Google HTTP Client 1.45.1** - HTTP client for Google APIs
- **Google Generative AI 1.0.0** - AI integration capabilities
- **Resend Java (LATEST)** - Email notification service
- **Java 17** - Runtime environment

## Security & Best Practices

### Security Measures
- Credential files excluded from version control via `.gitignore`
- Environment variables recommended for production configuration
- Regular dependency updates for security patches
- Immediate credential rotation if compromised

### Input Validation
- Real-time validation for all user inputs
- Protection against injection attacks
- Length limits on text inputs (100 characters for event titles)
- Type validation for numeric inputs

### Error Handling
- Comprehensive error catching and user-friendly messages
- Graceful degradation for service failures
- Detailed logging for troubleshooting
- System integrity validation on startup

## Development & Deployment

### Local Development
1. Clone repository
2. Configure credentials in `src/main/resources/`
3. Run `./mvnw spring-boot:run`
4. Access console interface automatically

### Production Deployment
1. Build JAR: `./mvnw clean package`
2. Set environment variables for credentials
3. Deploy JAR to server
4. Run: `java -jar TSS-Calendar-0.0.1-SNAPSHOT.jar`

### Docker Deployment
```bash
docker build -t tss-calendar-backend .
docker run -e FIREBASE_CREDENTIALS_JSON="$(cat firebase-credential.json)" \
           -e FIREBASE_PROJECT_ID="tss-calendar-a03ad" \
           -p 8080:8080 \
           tss-calendar-backend
```

## Logging & Monitoring

- **Log Location**: `logs/tss-calendar.log`
- **Log Levels**: Configurable in `application.properties`
- **System Validation**: Built-in health checks and integrity tests
- **Error Tracking**: Comprehensive error logging and reporting

## API Documentation

### REST Endpoints
- `GET /api/forms/responses` - Retrieve Google Forms responses
- `GET /firestore/add` - Fetch responses and store in Firestore

### Console Commands
- Interactive menu system with 8 main options
- Real-time validation and error handling
- Comprehensive test suite (Option 7)
- System integrity checks on startup

## References & Resources

### Official Documentation
- **Spring Boot**: https://spring.io/projects/spring-boot
- **Firebase Admin SDK**: https://firebase.google.com/docs/admin/setup
- **Google Forms API**: https://developers.google.com/workspace/forms/api
- **Google Drive API**: https://developers.google.com/drive/api/v3/about-sdk
- **Resend API (Java)**: https://resend.com/java
- **Google Generative AI**: https://ai.google.dev/gemini-api/docs#java

### Tutorials & Guides
- Spring Boot Tutorial for Beginners [2025]: https://youtu.be/gJrjgg1KVL4?si=rDTwvKmlFe0j_1oP
- Spring Boot Full Course 2025: https://youtu.be/Cw0J6jYJtzw?si=6SuQ7WQYcLopihDm
- Google Forms API Integration: https://gae-piaz.medium.com/creating-a-quiz-and-reading-responses-with-google-form-java-client-api-d74050ac73f8

---

## License

This project is for educational and internal use. Please ensure compliance with Google API terms of service and Firebase usage policies.

## Support

For issues and questions:
1. Check the console validation output (Option 7)
2. Review log files in `logs/tss-calendar.log`
3. Verify credential configuration
4. Test API connectivity and permissions