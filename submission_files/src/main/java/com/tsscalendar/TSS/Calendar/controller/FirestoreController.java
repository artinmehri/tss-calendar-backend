package com.tsscalendar.TSS.Calendar.controller;

import com.google.api.services.forms.v1.model.FormResponse;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.tsscalendar.TSS.Calendar.service.Firestore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * FirestoreController
 *
 * Handles converting Google Form responses into Firestore event documents.
 *
 * pre: GoogleFormController.getResponses() returns a map that contains a "responses" entry
 *      with a List<FormResponse>
 * post: For each form response a new Firestore document is added if it does not already exist.
 */
@RestController
@RequestMapping("/firestore")
public class FirestoreController {

    @Autowired
    private Firestore firestoreService;

    @Autowired
    private GoogleFormController googleFormController;

    /**
     * Adds form responses to Firestore as events.
     *
     * pre: Google Forms API credentials must be available to the GoogleForm service.
     * post: New events with status "pending" are added to the "events" collection if title is not duplicate.
     *
     * @return ResponseEntity with success=true or success=false and error message
     */
    @GetMapping("/add")
    public ResponseEntity<Map<String, Object>> addToFirestore() {

        Map<String, Object> response = new HashMap<>();
        try {
            Map<String, Object> answers = googleFormController.getResponses();

            @SuppressWarnings("unchecked")
            List<FormResponse> formResponses = (List<FormResponse>) answers.get("responses");

            if (formResponses == null || formResponses.isEmpty()) {
                response.put("success", true);
                response.put("totalAdded", 0);
                return ResponseEntity.ok(response);
            }

            int addedCount = 0;

            // Loop through all form responses
            for (FormResponse formResponse : formResponses) {
                try {
                    // Use helper to safely extract text answers by question ID
                    String eventTitle = getTextAnswer(formResponse, "46cfc9f8");
                    String eventSupervisor = getTextAnswer(formResponse, "03e3278b");
                    String eventDate = getTextAnswer(formResponse, "2171d758");
                    String eventTime = getTextAnswer(formResponse, "0db76540");
                    String eventDescription = getTextAnswer(formResponse, "5235d67f");
                    String eventCategory = getTextAnswer(formResponse, "6082cc62");
                    String weeklyRaw = getTextAnswer(formResponse, "789c6989");

                    String submitTime = formResponse.getCreateTime();
                    String respondentEmail = formResponse.getRespondentEmail();

                    Boolean weekly = null;
                    if (weeklyRaw != null) {
                        weekly = "yes".equalsIgnoreCase(weeklyRaw.trim());
                    }

                    if (eventTitle == null || eventTitle.isBlank()) {
                        // Skip responses without a title
                        continue;
                    }

                    // Check duplicate by title (existing behavior). Firestore checks title field.
                    if (!firestoreService.checkDocumentExists(eventTitle)) {
                        firestoreService.addEvent(eventTitle, eventSupervisor, eventDate, eventTime, eventDescription, eventCategory, weekly, submitTime, respondentEmail);
                        addedCount++;
                    }
                } catch (Exception e) {
                    // Log error and continue with next response
                    System.err.println("Error processing response: " + e.getMessage());
                }
            }

            response.put("success", true);
            response.put("totalAdded", addedCount);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * Safely extract the first text answer for a question id from a FormResponse.
     *
     * pre: formResponse may be null or contain no answers
     * post: returns the string value of the first text answer or null if not present
     *
     * @param formResponse the FormResponse from Google Forms API
     * @param questionId the question id to look up in the answers map
     * @return the text answer or null
     */
    private String getTextAnswer(FormResponse formResponse, String questionId) {
        if (formResponse == null || formResponse.getAnswers() == null) {
            return null;
        }
        var answer = formResponse.getAnswers().get(questionId);
        if (answer == null) {
            return null;
        }
        var textAnswers = answer.getTextAnswers();
        if (textAnswers == null || textAnswers.getAnswers() == null || textAnswers.getAnswers().isEmpty()) {
            return null;
        }
        return textAnswers.getAnswers().get(0).getValue();
    }
}