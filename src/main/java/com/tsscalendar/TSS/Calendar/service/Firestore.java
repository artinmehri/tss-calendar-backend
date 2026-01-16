package com.tsscalendar.TSS.Calendar.service;
import com.google.api.services.forms.v1.FormsScopes;
import com.google.auth.oauth2.GoogleCredentials;
import java.io.FileInputStream;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
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

    public boolean checkDocumentExists(String eventTitle) throws ExecutionException, InterruptedException {
            com.google.cloud.firestore.Firestore db = FirestoreClient.getFirestore();
            // asynchronously retrieve multiple documents
            ApiFuture<QuerySnapshot> future = db.collection("events").whereEqualTo("title", eventTitle).get();
            // future.get() blocks on response
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            // return a boolean on whether the document exists or no
            return !documents.isEmpty();
    }

    public List<String> getAllEventsStatusBased(String status) throws ExecutionException, InterruptedException {
        com.google.cloud.firestore.Firestore db = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = db.collection("events").whereEqualTo("status", status).get();
        // Returning all the documents with that status
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        List<String> formattedEvents = new ArrayList<>();

        for (QueryDocumentSnapshot document : documents) {
            String eventInfo = String.format(
                    "Event ID: %s\nTitle: %s\nDescription: %s\nDate: %s\nTime: %s\nRespondent Email: %s\nCategory: %s\nSupervisor: %s\nIs It Weekly?:%s",
                    document.getId(),
                    document.getString("title"),
                    document.getString("description"),
                    document.getString("date"),
                    document.getString("time"),
                    document.getString("respondentEmail"),
                    document.getString("category"),
                    document.getString("supervisor"),
                    document.getBoolean("weekly")
                    );
            formattedEvents.add(eventInfo);
        }
        return formattedEvents;
    }
    
    public void approveEvent(String eventTitle) throws ExecutionException, InterruptedException {
        com.google.cloud.firestore.Firestore db = FirestoreClient.getFirestore();
        String eventId = "";
        // Find event by title
        var query = db.collection("events")
                .whereEqualTo("title", eventTitle)
                .get()
                .get();

        for (DocumentSnapshot doc : query.getDocuments()) {
            eventId = doc.getId();  // Get the ID
            // Now you can reference it
        }

        // Update an existing document
        DocumentReference docRef = db.collection("events").document(eventId);

        // (async) Update one field
        ApiFuture<WriteResult> future = docRef.update("status", "approved");

        WriteResult result = future.get();
    }


    public void declineEvent(String eventTitle) throws ExecutionException, InterruptedException {
        com.google.cloud.firestore.Firestore db = FirestoreClient.getFirestore();
        String eventId = "";
        // Find event by title
        var query = db.collection("events")
                .whereEqualTo("title", eventTitle)
                .get()
                .get();

        for (DocumentSnapshot doc : query.getDocuments()) {
            eventId = doc.getId();  // Get the ID
            // Now you can reference it
        }

        // Update an existing document
        DocumentReference docRef = db.collection("events").document(eventId);

        // (async) Update one field
        ApiFuture<WriteResult> future = docRef.update("status", "declined");

        WriteResult result = future.get();
        System.out.println("Write result: " + result);
    }


    public void addEvent(String eventTitle, String eventSupervisor, String eventDate, String eventTime, String eventDescription, String eventCategory, Boolean weekly, String submitTime, String respondentEmail) throws ExecutionException, InterruptedException {
        // Ensure Firebase is initialized before using Firestore
        if (!initialized) {
            throw new IllegalStateException("Firestore not initialized. Call constructor first.");
        }
        
        // Get Firestore instance - this requires Firebase to be initialized
        com.google.cloud.firestore.Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection("events").document();
        
        // Add document data using the parameters passed to the method
        Map<String, Object> data = new HashMap<>();
        data.put("title", eventTitle);
        data.put("supervisor", eventSupervisor);
        data.put("date", eventDate);
        data.put("time", eventTime);
        data.put("description", eventDescription);
        data.put("category", eventCategory);
        data.put("weekly", weekly);
        data.put("status", "pending");
        data.put("submitTime",submitTime );
        data.put("respondentEmail", respondentEmail);

        // Asynchronously write data
        ApiFuture<WriteResult> result = docRef.set(data);
    }
}