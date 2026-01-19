# TSS Calendar Backend

Spring Boot application that fetches Google Form submissions, stores events in Firestore, and provides admin controls via console menu and REST endpoints.

> **IMPORTANT**: Service account credential JSON files must NOT be committed to the repository. If you previously committed credentials, rotate/delete the keys immediately.

## Overview

This backend:
- Reads Google Forms responses using Google Forms API
- Adds events to Firebase Firestore (status defaults to `pending`)
- Allows admin to view, approve, and decline events via console menu
- Exposes REST endpoints for external access
- Uses Java 17+, Spring Boot 4.0.0

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

The application provides an interactive console menu:

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
[6] Send Email (test)
[7] Activate AI (if available)
[0] Exit
```

**Menu Options:**
- **Option 1**: Fetches Google Forms responses and adds new events to Firestore
- **Option 2**: Displays all pending events
- **Option 3**: Approves an event by exact title
- **Option 4**: Declines an event by exact title
- **Option 5**: Shows all approved events
- **Option 6-7**: Testing features

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
│   │   ├── TssCalendarApplication.java
│   │   ├── controller/
│   │   │   ├── FirestoreController.java
│   │   │   └── GoogleFormController.java
│   │   ├── service/
│   │   │   ├── Firestore.java
│   │   │   ├── GoogleForm.java
│   │   │   └── AiClient.java
│   │   └── EmailSend.java
│   └── resources/
│       ├── application.properties
│       ├── credential.json (gitignored)
│       ├── firebase-credential.json (gitignored)
│       └── static/
└── test/
    └── java/
```

## Dependencies

- Spring Boot 4.0.0
- Firebase Admin SDK 9.7.0
- Google Forms API v1-rev20250422-2.0.0
- Google Drive API v3-rev20240914-2.0.0
- Google Generative AI 1.0.0
- Resend Java (LATEST)
- Java 25

## Security Notes

- Credential files are excluded from version control via `.gitignore`
- Use environment variables for sensitive configuration in production
- Regularly update dependencies for security patches
- If credentials were ever committed publicly, rotate them immediately

## Audit Fields

The system tracks approval/decline actions with the following fields:
- `approvedBy` - User who approved the event
- `approvedAt` - Timestamp when approved
- `declinedBy` - User who declined the event  
- `declinedAt` - Timestamp when declined

## Title Normalization

Events are stored with both original `title` and normalized `title_lower` fields to enable case-insensitive searching and partial matching in the console approve/decline functions.

## Sources

### Video Tutorials
- Spring Boot Tutorial for Beginners [2025]: https://youtu.be/gJrjgg1KVL4?si=rDTwvKmlFe0j_1oP
- Spring Boot Tutorial for Beginners | Full Course 2025: https://youtu.be/Cw0J6jYJtzw?si=6SuQ7WQYcLopihDm

### API Documentation
- Google Forms API: https://developers.google.com/workspace/forms/api/reference/rest?_gl=1*160uzdj*_up*MQ..*_ga*MjA0NjM2OTExNi4xNzY4NjI5Mjc3*_ga_SM
- Resend API (Java): https://resend.com/java
- Firestore API: https://firebase.google.com/docs/firestore
- Gemini API (Java): https://ai.google.dev/gemini-api/docs#java

### Sample Code & Tutorials
- Google Forms API sample app: https://gae-piaz.medium.com/creating-a-quiz-and-reading-responses-with-google-form-java-client-api-d74050ac73f8