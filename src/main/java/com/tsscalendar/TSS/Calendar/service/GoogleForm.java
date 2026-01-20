/**
 * GoogleForm. java
 *
 * Service class for interacting with Google Forms API.
 * Handles authentication and API calls to retrieve form responses.
 *
 * Sources:
 * - Google Forms API sample: https://gae-piaz.medium.com/creating-a-quiz-and-readil
 * - Google Auth Library: https://github.com/googleapis/google-auth-library-java
 *
 * @author Artin Mehri
 * @version 1.0
 */
package com.tsscalendar.TSS.Calendar.service;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.forms.v1.Forms;
import com.google.api.services.forms.v1.FormsScopes;
import com.google.api.services.forms.v1.model.ListFormResponsesResponse;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.stereotype.Service;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;


/**
 * Service for Google Forms API operations.
 * Provide methods to authenticate and retrieve form responses.
 */
@Service
public class GoogleForm {

    // Constants for Google API configuration
    private static final String APPLICATION_NAME = "TSS Calendar Backend";
    private static final String CREDENTIALS_FILE = "src/main/resources/tss-calendar-483202-30610a4d1760.json";


    private Forms formsService;

    /**
     * Constructor that initializes Google Forms API connection.
     * Creates authenticated Forms service using service account credentials.
     *
     * @pre credentials.json must exist at CREDENTIALS_FILE path with valid service account key
     * @post formsService is initialized and ready to make API calls
     *
     * @throws GeneralSecurityException if SSL/TLS setup fails
     * @throws IOException if credential file cannot be read
     */
    public GoogleForm() throws GeneralSecurityException, IOException {
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        this.formsService = new Forms.Builder(GoogleNetHttpTransport.newTrustedTransport(),jsonFactory,null).setApplicationName(APPLICATION_NAME).build();

    }

    // Getting access token from the local file and getting access to everything (read, write, etc...)
    private String getAccessToken() throws IOException {

            // Load service account credentials from file
            GoogleCredentials credential = GoogleCredentials.fromStream(new FileInputStream(CREDENTIALS_FILE)).createScoped(FormsScopes.all());

            // If there is already a token use it, otherwise ask Google for a new one (sometimes the tokens are cashed)
            if (credential.getAccessToken() != null) {
                return credential.getAccessToken().getTokenValue();
            } else {
                // If no token exist, refresh it to get a new one
                return credential.refreshAccessToken().getTokenValue();
            }
    }

    /**
     * Retrieves all responses for a specific Google Form.
     *
     * @pre formsService must be initialized with valid credentials
     * @pre formId must be a valid Google Form ID
     * @post Returns all responses submitted to the form
     *
     * @param formId the unique identifier of the Google Form
     * @return ListFormResponsesResponse containing all form submissions
     * @throws IOException if API call fails or network error occurs
     */
    public ListFormResponsesResponse getFormResponses(String formId) throws IOException {
            return formsService.forms().responses().list(formId).setAccessToken(getAccessToken()).execute();
    }
}