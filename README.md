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
- Maven 3.6+
- Internet access for Google APIs & Firestore
- Firebase service account JSON for Firestore access
- Google credentials JSON for Google Forms API

## Setup & Run

### 1. Build the application
```bash
mvn clean package
```

### 2. Configure credentials

**Option A - File paths (recommended):**
```bash
export FIREBASE_CREDENTIAL_FILE=/path/to/firebase-credential.json
export GOOGLE_CREDENTIAL_FILE=/path/to/credential.json
```

**Option B - Environment variables:**
```bash
export FIREBASE_CREDENTIAL_JSON="$(cat /path/to/firebase-credential.json)"
export GOOGLE_CREDENTIAL_JSON="$(cat /path/to/credential.json)"
```

### 3. Run the application
```bash
java -jar target/tss-calendar-backend-0.0.1-SNAPSHOT.jar
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
```

## Testing

Run unit tests:
```bash
mvn test
```

## JavaDoc

Generate API documentation:
```bash
mvn javadoc:javadoc
```
Output: `target/site/apidocs/`

## Project Structure

```
src/
├── main/
│   ├── java/com/tsscalendar/TSS/Calendar/
│   │   ├── TssCalendarApplication.java
│   │   ├── controller/
│   │   ├── service/
│   │   └── ...
│   └── resources/
│       ├── application.properties
│       ├── credential.json (gitignored)
│       ├── firebase-credential.json (gitignored)
│       └── static/
└── test/
```

## Dependencies

- Spring Boot 4.0.0
- Firebase Admin SDK 9.7.0
- Google Forms API
- Google Drive API
- Google Generative AI
- Resend Java
- Java 25

## Security Notes

- Credential files are excluded from version control via `.gitignore`
- Use environment variables for sensitive configuration in production
- Regularly update dependencies for security patches
- If credentials were ever committed publicly, rotate them immediately

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