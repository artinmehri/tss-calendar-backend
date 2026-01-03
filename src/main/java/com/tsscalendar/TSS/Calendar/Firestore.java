package com.tsscalendar.TSS.Calendar;
import com.google.auth.oauth2.GoogleCredentials;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class Firestore {
    private GoogleCredentials credentials;
    private FirebaseOptions options;
    private boolean initialized = false;

    public Firestore() throws IOException {
        // Check if Firebase is already initialized to avoid IllegalStateException
        if (FirebaseApp.getApps().isEmpty()) {
            credentials = GoogleCredentials.getApplicationDefault();
            options = FirebaseOptions.builder()
                    .setCredentials(credentials)
                    .setProjectId("tss-calendar-a03ad")
                    .build();
            FirebaseApp.initializeApp(options);
        }
        initialized = true;
    }


    public void addData() throws ExecutionException, InterruptedException {
        // Ensure Firebase is initialized before using Firestore
        if (!initialized) {
            throw new IllegalStateException("Firestore not initialized. Call constructor first.");
        }
        
        // Get Firestore instance - this requires Firebase to be initialized
        com.google.cloud.firestore.Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection("events").document("example");
        
        // Add document data with id "example" using a hashmap
        Map<String, Object> data = new HashMap<>();
        data.put("first", "Ada");
        data.put("last", "Lovelace");
        data.put("born", 1815);
        
        // Asynchronously write data
        ApiFuture<WriteResult> result = docRef.set(data);
        // result.get() blocks on response
        System.out.println("Update time : " + result.get().getUpdateTime());
    }
}