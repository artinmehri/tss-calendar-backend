package com.tsscalendar.TSS.Calendar.service;
import com.google.api.services.forms.v1.FormsScopes;
import com.google.auth.oauth2.GoogleCredentials;
import java.io.FileInputStream;
import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.time.*;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.google.cloud.Timestamp;
import java.time.LocalDate;
import java.util.Date;import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class Firestore {
    private FirebaseOptions options;
    private boolean initialized = false;
    private static final String CREDENTIALS_FILE = "src/main/resources/firebase-credential.json";


    public Firestore() throws IOException {
        // Check if Firebase is already initialized to avoid IllegalStateException
        if (FirebaseApp.getApps().isEmpty()) {
            GoogleCredentials credential = GoogleCredentials.fromStream(new FileInputStream(CREDENTIALS_FILE)).createScoped(FormsScopes.all());

            options = FirebaseOptions.builder()
                    .setCredentials(credential)
                    .setProjectId("tss-calendar-a03ad")
                    .build();
            FirebaseApp.initializeApp(options);
        }
        initialized = true;
    }

    private String dayOfTheWeek(String date) {
        // 1. Parse the date string into a LocalDate object
        LocalDate localDate = LocalDate.parse(date);

        // 2. Get the DayOfWeek enum
        DayOfWeek dayOfWeek = localDate.getDayOfWeek();

        // The DayOfWeek enum itself prints as a simple string (e.g., "MONDAY")
        System.out.println("DayOfWeek enum value: " + dayOfWeek);

        // 3. Format the DayOfWeek enum into a full display name (e.g., "Monday")
        return(dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH));
    }


    public void addData() throws ExecutionException, InterruptedException {
        // Ensure Firebase is initialized before using Firestore
        if (!initialized) {
            throw new IllegalStateException("Firestore not initialized. Call constructor first.");
        }
        
        // Get Firestore instance - this requires Firebase to be initialized
        com.google.cloud.firestore.Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection("events").document("App Club");
        
        // Add document data with id "example" using a hashmap
        Map<String, Object> data = new HashMap<>();
        data.put("eventTitle", "App Club Meeting");
        data.put("eventCategory", "Student Life");
        data.put("description", "This is App Club's weekly meeting");
        data.put("time", "15:45");
        data.put("date", "2026-01-12");
        data.put("weekDay", dayOfTheWeek("2026-01-12"));
        data.put("weekly", true);
        
        // Asynchronously write data
        ApiFuture<WriteResult> result = docRef.set(data);
        // result.get() blocks on response
        System.out.println("Update time : " + result.get().getUpdateTime());
    }
}