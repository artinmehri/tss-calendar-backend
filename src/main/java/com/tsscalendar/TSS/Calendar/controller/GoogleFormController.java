/**
 * GoogleFormController.java
 *
 * REST controller for handling Google Forms API interactions.
 * Provides endpoints to retrieve form responses from Google Forms.
 *
 * Sources:
 * - Google Forms API Documentation: https://developers.google.com/workspace/forms/api/reference/rest
 * - Spring Boot REST Tutorial: https://youtu.be/gJrjgg1KVL4?si=rDTwvKmlFe0j_1oP
 *
 * @author Artin Mehri
 * @version 1.0
 */
package com.tsscalendar.TSS.Calendar.controller;
import com.tsscalendar.TSS.Calendar.service.GoogleForm;
import com.google.api.services.forms.v1.model.ListFormResponsesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller class for Google Forms API endpoints.
 * Handles HTTP requests related to form response retrieval.
 */
@RestController
@RequestMapping("/api/forms")
public class GoogleFormController {

    // constant for Google Form ID
    final String FORM_ID = "1Eeiyjyh2ACJdDQAF1IBN0eocbPsjh2I8CvVAEOXlDOo";


    @Autowired
    private GoogleForm formsService;

    /**
     * Retrieves all responses from the configured Google Form.
     *
     * @pre formsService must be initialized with valid Google API credentials
     * @post Returns a map containing success status and form responses
     *
     * @return Map containing either:
     *         - success: true, totalResponses: count, responses: list of FormResponse objects
     *         - success: false, error: error message string
     */
    @GetMapping("/responses")
    public Map<String, Object> getResponses() {
        try {
            // Fetch responses from Google forms API
            // https://docs.google.com/forms/d/YOUR_FORM_ID/edit
            ListFormResponsesResponse response = formsService.getFormResponses(FORM_ID);

            // Build success response with form data
            Map<String, Object> answers = new HashMap<>();
            answers.put("success", true);
            answers.put("totalResponses", response.getResponses() != null ? response.getResponses().size() : 0);
            answers.put("responses", response.getResponses());

            return answers;

        } catch (Exception e) {
            // Build error response if API call fails
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("error", e.getMessage());
            return error;
        }
    }
}