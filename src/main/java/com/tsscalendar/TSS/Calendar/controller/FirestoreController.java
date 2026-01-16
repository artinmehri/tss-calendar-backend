package com.tsscalendar.TSS.Calendar.controller;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.tsscalendar.TSS.Calendar.service.Firestore;
import com.google.api.services.forms.v1.model.FormResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/firestore")
public class FirestoreController {

    @Autowired
    private Firestore firestoreService;
    @Autowired
    private GoogleFormController googleFormController;


    @GetMapping("/add")
    public ResponseEntity<Map<String, Object>> addToFirestore() {

        Map<String, Object> response = new HashMap<>();
        try {
            Map<String, Object> answers = googleFormController.getResponses();

            // Get the responses list
            @SuppressWarnings("unchecked")
            List<FormResponse> formResponses = (List<FormResponse>) answers.get("responses");

            // Loop through all form responses
            for (FormResponse formResponse : formResponses) {

                try {
                    // Extract event title from question ID "46cfc9f8"
                    String eventTitle = null;
                    var answer = formResponse.getAnswers().get("46cfc9f8");
                    if (answer != null && answer.getTextAnswers() != null &&
                        answer.getTextAnswers().getAnswers() != null &&
                        !answer.getTextAnswers().getAnswers().isEmpty()) {
                        eventTitle = answer.getTextAnswers().getAnswers().get(0).getValue();
                    }

                    String eventSupervisor = null;
                    answer = formResponse.getAnswers().get("03e3278b");
                    if (answer != null && answer.getTextAnswers() != null &&
                            answer.getTextAnswers().getAnswers() != null &&
                            !answer.getTextAnswers().getAnswers().isEmpty()) {
                        eventSupervisor = answer.getTextAnswers().getAnswers().get(0).getValue();
                    }

                    String eventDate = null;
                    answer = formResponse.getAnswers().get("2171d758");
                    if (answer != null && answer.getTextAnswers() != null &&
                            answer.getTextAnswers().getAnswers() != null &&
                            !answer.getTextAnswers().getAnswers().isEmpty()) {
                        eventDate = answer.getTextAnswers().getAnswers().get(0).getValue();
                    }

                    String eventTime = null;
                    answer = formResponse.getAnswers().get("0db76540");
                    if (answer != null && answer.getTextAnswers() != null &&
                            answer.getTextAnswers().getAnswers() != null &&
                            !answer.getTextAnswers().getAnswers().isEmpty()) {
                        eventTime = answer.getTextAnswers().getAnswers().get(0).getValue();
                    }

                    String eventDescription = null;
                    answer = formResponse.getAnswers().get("5235d67f");
                    if (answer != null && answer.getTextAnswers() != null &&
                            answer.getTextAnswers().getAnswers() != null &&
                            !answer.getTextAnswers().getAnswers().isEmpty()) {
                        eventDescription = answer.getTextAnswers().getAnswers().get(0).getValue();
                    }

                    String eventCategory = null;
                    answer = formResponse.getAnswers().get("6082cc62");
                    if (answer != null && answer.getTextAnswers() != null &&
                            answer.getTextAnswers().getAnswers() != null &&
                            !answer.getTextAnswers().getAnswers().isEmpty()) {
                        eventCategory = answer.getTextAnswers().getAnswers().get(0).getValue();
                    }

                    // Get create time (A built-in function in Google Forms API)
                    String submitTime = formResponse.getCreateTime();

                    // Get respondent email (A built-in function in Google Forms API)
                    String respondentEmail = formResponse.getRespondentEmail();

                    Boolean weekly = null;
                    answer = formResponse.getAnswers().get("789c6989");
                    if (answer != null && answer.getTextAnswers() != null &&
                            answer.getTextAnswers().getAnswers() != null &&
                            !answer.getTextAnswers().getAnswers().isEmpty()) {
                        if ("Yes".equals(answer.getTextAnswers().getAnswers().get(0).getValue())) {
                            weekly = true;
                        } else {
                            weekly = false;
                        }
                    }

                    if (!(firestoreService.checkDocumentExists(eventTitle))) {
                        // Add data to Firestore
                       firestoreService.addEvent(eventTitle, eventSupervisor, eventDate, eventTime, eventDescription, eventCategory, weekly, submitTime, respondentEmail);
                    }

                    System.out.println("Fetched all the events from Google Form and Added to Firestore");
                } catch (Exception e) {
                    System.err.println("Error processing response: " + e.getMessage());
                }
            }
            // Returning OK!
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}