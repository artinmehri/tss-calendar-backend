package com.tsscalendar.TSS.Calendar.service;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import com.tsscalendar.TSS.Calendar.EmailSend;
import com.tsscalendar.TSS.Calendar.controller.FirestoreController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tsscalendar.TSS.Calendar.service.Firestore;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class AiClient {

    @Autowired
    private Firestore firestoreService;
    
    @Autowired
    private FirestoreController firestoreController;


    public void writeEmail() throws ExecutionException, InterruptedException {
        List<String> declinedEvents;
        declinedEvents = firestoreService.getAllEventsStatusBased("declined");

        String apiKey = "AIzaSyCMnqw_awYHHRi436NGpf6IJqD_M6Q0TsI";
        Client client = Client.builder().apiKey(apiKey).build();

        GenerateContentResponse response =
                client.models.generateContent(
                        "gemini-2.0-flash",
                        "You are a high school vice principal. Your job is to write an email to the people who submitted the following events, these events are declined, in the email you need to specify why the event is declined and how they could make the appropriate adjustments and changes to get their event approved. Your response format should be the following: on the first line it has to have the respondent email and only their full email nothing else, on the next line it has to have the exact appropriate subject line, and on the third line it has to have the body of the email in html and css format, fully enclosed inside a html tag, the theme of the style of the emails should be ##ffe21f and  ##252122 as these are the main theme of the school and the high school is known as home of tigers at anytime if you need to mention who this email is comig from never mention AI or Vice principal, you always refer to yourself or the sender of email as TSS Calendar. After you list the email, next line subject line, next line html, leave one line empty after the html and move to the next event." + declinedEvents,
                        null);
        String aiResponse = response.text();
        System.out.println("AI Email Response: " + aiResponse);
        String[] lines = aiResponse.split("\n");
        System.out.println("Total lines: " + lines.length);
        
        for (int i = 0; i < lines.length; i += 4) {
            while (i < lines.length && lines[i].trim().isEmpty()) i++;
            if (i + 2 < lines.length) {
                String email = lines[i].trim();
                String subject = lines[i + 1].trim();
                String html = lines[i + 2].trim();
                System.out.println("Processing: email=" + email + ", subject=" + subject + ", html length=" + html.length());
                
                if (!email.isEmpty() && !subject.isEmpty() && !html.isEmpty()) {
                    EmailSend.send(email, subject, html);
                }
                Thread.sleep(1000);
            }
        }
    }


    public void getAiResponse() throws ExecutionException, InterruptedException {
        firestoreController.addToFirestore();

        List<String> pendingEvents;
        pendingEvents = firestoreService.getAllEventsStatusBased("pending");

        String eventsText;
        if (pendingEvents.isEmpty()) {
            eventsText = " No pending events to review.";
        } else {
            eventsText = " Here are the pending events to review:\n" + String.join("\n\n", pendingEvents);
        }

        String apiKey = "AIzaSyD5l4nh7Sf4murkxPKYgDs8xONjCPzdG8M";
        Client client = Client.builder().apiKey(apiKey).build();

        GenerateContentResponse response =
                client.models.generateContent(
                        "gemini-2.0-flash",
                        "You are a high school vice principal. Your job is to decide whether the following events with their descriptions and all the other information are school appropriate to be available to all the high school students through a wide calendar app, if you think information is inaccurate or inappropriate, you reject it. On the first line of your response you have to write Approved and the lines after , one line after another are the title of the events listed, only the TITLES, if there are no approved events then leave only one line blank, after listing all the approved events, on one line you should type Declined: and list the declined events the same way. Your response should only include what has been mentioned, nothing else!" + eventsText,
                        null);
                String aiResponse =  response.text();

                // Parse the AI response
                String[] lines = aiResponse.split("\n");
                boolean approvedSection = false;
                boolean declinedSection = false;
                
                for (String line : lines) {
                    line = line.trim();
                    
                    if (line.equals("Approved")) {
                        approvedSection = true;
                        declinedSection = false;
                        continue;
                    } else if (line.equals("Declined:")) {
                        approvedSection = false;
                        declinedSection = true;
                        continue;
                    }
                    
                    if (approvedSection && !line.isEmpty()) {
                        // Approve the event
                        firestoreService.approveEvent(line);
                    } else if (declinedSection && !line.isEmpty()) {
                        // Decline the event
                        firestoreService.declineEvent(line);
                    }
                }
                
                // Send emails for declined events
                writeEmail();
    }
}