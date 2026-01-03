package com.tsscalendar.TSS.Calendar.controller;
import com.tsscalendar.TSS.Calendar.service.GoogleForm;
import com.google.api.services.forms.v1.model.ListFormResponsesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/forms")
public class GoogleFormController {

    @Autowired
    private GoogleForm formsService;

    @GetMapping("/responses")
    public Map<String, Object> getResponses() {
        try {
            // Get your form ID from the URL:
            // https://docs.google.com/forms/d/YOUR_FORM_ID/edit
            final String FORM_ID = "1Eeiyjyh2ACJdDQAF1IBN0eocbPsjh2I8CvVAEOXlDOo";

            ListFormResponsesResponse response = formsService.getFormResponses(FORM_ID);

            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("totalResponses", response.getResponses() != null ? response.getResponses().size() : 0);
            result.put("responses", response.getResponses());

            System.out.println(result);
            return result;

        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("error", e.getMessage());
            e.printStackTrace();
            return error;
        }
    }
}