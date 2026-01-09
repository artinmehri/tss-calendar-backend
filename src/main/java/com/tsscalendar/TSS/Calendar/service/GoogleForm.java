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
@Service
public class GoogleForm {
    private static final String APPLICATION_NAME = "TSS Calendar Backend";
    private static final String CREDENTIALS_FILE = "src/main/resources/credential.json";
    private Forms formsService;

    // Creating connection to Google API
    public GoogleForm() throws GeneralSecurityException, IOException {
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        this.formsService = new Forms.Builder(GoogleNetHttpTransport.newTrustedTransport(),jsonFactory,null).setApplicationName(APPLICATION_NAME).build();

    }

    // Getting access token from the local file and getting access to everything (read, write, etc...)
    private String getAccessToken() throws IOException {
        GoogleCredentials credential = GoogleCredentials.fromStream(new FileInputStream(CREDENTIALS_FILE)).createScoped(FormsScopes.all());

        // If there is already a token use it, otherwise ask Google for a new one (sometimes the tokens are cashed)
        if (credential.getAccessToken() != null) {
            return credential.getAccessToken().getTokenValue();
        } else {
            return credential.refreshAccessToken().getTokenValue();
        }
    }

    public ListFormResponsesResponse getFormResponses(String formId) throws IOException {
        ListFormResponsesResponse result = formsService.forms().responses().list(formId).setAccessToken(getAccessToken()).execute();
        System.out.println(result);
        return result;
    }
}