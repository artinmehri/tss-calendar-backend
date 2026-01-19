package com.tsscalendar.TSS.Calendar.service;
import com.google.api.services.forms.v1.FormsScopes;
import com.google.auth.oauth2.GoogleCredentials;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;


/**
 * Service class for managing Firebase Firestore operations.
 * Handles event storage, retrieval, approval, and decline operations.
 * 
 * @pre Firebase project is configured and credentials are available
 * @post Firestore connection is established and ready for operations
 * @author Artin Mehri
 * @version 1.0
 */
@Service
public class Firestore {
    private FirebaseOptions options;
    private boolean initialized = false;
    private static final String PROJECT_ID = "tss-calendar-a03ad";

    /**
     * Constructor for Firestore service.
     * Initializes Firebase connection using credential files.
     * 
     * @pre Firebase credential files are available in project
     * @post Firebase is initialized and ready for Firestore operations
     * @throws IOException If credentials cannot be loaded or Firebase initialization fails
     */
    public Firestore() throws IOException {
        // Check if Firebase is already initialized to avoid IllegalStateException
        if (FirebaseApp.getApps().isEmpty()) {
            GoogleCredentials credential = GoogleCredentials.fromStream(new java.io.FileInputStream("src/main/resources/static/firebase-credential.json"))
                    .createScoped(FormsScopes.all());
            
            options = FirebaseOptions.builder()
                    .setCredentials(credential)
                    .setProjectId(PROJECT_ID)
                    .build();
            FirebaseApp.initializeApp(options);
        }
        initialized = true;
    }

    /**
     * Checks if a document with the given event title exists in Firestore.
     * 
     * @pre Firestore is initialized and accessible
     * @post Boolean indicating document existence is returned
     * @param eventTitle The title of the event to check
     * @return true if document exists, false otherwise
     * @throws ExecutionException If Firestore query execution fails
     * @throws InterruptedException If Firestore query is interrupted
     */
    public boolean checkDocumentExists(String eventTitle) throws ExecutionException, InterruptedException {
            com.google.cloud.firestore.Firestore db = FirestoreClient.getFirestore();
            // asynchronously retrieve multiple documents
            ApiFuture<QuerySnapshot> future = db.collection("events").whereEqualTo("title", eventTitle).get();
            // future.get() blocks on response
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            // return a boolean on whether the document exists or no
            return !documents.isEmpty();
    }

    /**
     * Retrieves all events with a specific status from Firestore.
     * 
     * @pre Firestore is initialized and accessible
     * @post List of formatted event strings with the specified status is returned
     * @param status The status to filter events by (e.g., "pending", "approved", "declined")
     * @return List of formatted event information strings
     * @throws ExecutionException If Firestore query execution fails
     * @throws InterruptedException If Firestore query is interrupted
     */
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
    
    /**
     * Approves an event by updating its status to "approved" in Firestore.
     * 
     * @pre Firestore is initialized and event with given title exists
     * @post Event status is updated to "approved" with audit fields
     * @param eventTitle The title of the event to approve
     * @throws ExecutionException If Firestore update operation fails
     * @throws InterruptedException If Firestore update is interrupted
     */
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
        
        if (eventId.isEmpty()) {
            System.out.println("No event found with title: " + eventTitle);
            return;
        }

        // Update an existing document
        DocumentReference docRef = db.collection("events").document(eventId);

        // (async) Update status and audit fields
        Map<String, Object> updates = new HashMap<>();
        updates.put("status", "approved");
        updates.put("approvedBy", "system");
        updates.put("approvedAt", java.time.Instant.now().toString());
        
        ApiFuture<WriteResult> future = docRef.update(updates);

        WriteResult result = future.get();
    }


    /**
     * Declines an event by updating its status to "declined" in Firestore.
     * 
     * @pre Firestore is initialized and event with given title exists
     * @post Event status is updated to "declined" with audit fields
     * @param eventTitle The title of the event to decline
     * @throws ExecutionException If Firestore update operation fails
     * @throws InterruptedException If Firestore update is interrupted
     */
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
        
        if (eventId.isEmpty()) {
            System.out.println("No event found with title: " + eventTitle);
            return;
        }

        // Update an existing document
        DocumentReference docRef = db.collection("events").document(eventId);

        // (async) Update status and audit fields
        Map<String, Object> updates = new HashMap<>();
        updates.put("status", "declined");
        updates.put("declinedBy", "system");
        updates.put("declinedAt", java.time.Instant.now().toString());
        
        ApiFuture<WriteResult> future = docRef.update(updates);

        WriteResult result = future.get();
        System.out.println("Write result: " + result);
    }


    /**
     * Adds a new event to Firestore with the provided details.
     * 
     * @pre Firestore is initialized and all parameters are valid
     * @post New event document is created in Firestore with "pending" status
     * @param eventTitle The title of the event
     * @param eventSupervisor The supervisor name
     * @param eventDate The event date
     * @param eventTime The event time
     * @param eventDescription The event description
     * @param eventCategory The event category
     * @param weekly Whether the event is weekly
     * @param submitTime The submission time
     * @param respondentEmail The respondent's email
     * @throws ExecutionException If Firestore write operation fails
     * @throws InterruptedException If Firestore write is interrupted
     */
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
        data.put("title_lower", eventTitle.toLowerCase()); // For case-insensitive search
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