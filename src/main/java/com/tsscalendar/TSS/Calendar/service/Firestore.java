package com.tsscalendar.TSS.Calendar.service;
import com.google.api.services.forms.v1.FormsScopes;
import com.google.auth.oauth2.GoogleCredentials;
import java.io.FileInputStream;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.HashMap;
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

    public boolean checkDocumentExists(String docId) {
        try {
        com.google.cloud.firestore.Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection("events").document(docId);
        ApiFuture<DocumentSnapshot> future = docRef.get();
            DocumentSnapshot document = future.get(); // Synchronously wait for the result

            if (document.exists()) {
                System.out.println("Document exists!");
                return true;
            } else {
                System.out.println("Document does not exist!");
                return false;
            }
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Failed with exception: " + e.getMessage());
            return false;
        }
    }

    public void approveEvent(String eventTitle) throws ExecutionException, InterruptedException {
        System.out.println("approving event");
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
        System.out.println("Write result: " + result);
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
        // result.get() blocks on response
        System.out.println("Update time : " + result.get().getUpdateTime());
    }
}