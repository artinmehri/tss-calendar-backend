package com.tsscalendar.TSS.Calendar;
import com.tsscalendar.TSS.Calendar.controller.FirestoreController;
import com.tsscalendar.TSS.Calendar.service.AiClient;
import com.tsscalendar.TSS.Calendar.service.Firestore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Scanner;
import java.util.List;

/**
 * Main Spring Boot application class for TSS Calendar Backend.
 * 
 * @pre Firebase credentials are properly configured via environment variables or credential files
 * @post Application starts with interactive console menu and web server on port 8080
 * @author Artin Mehri
 * @version 1.0
 */
@SpringBootApplication
public class TssCalendarApplication {
    
    /**
     * Main entry point for the Spring Boot application.
     * 
     * @pre Java 17+ is installed and all dependencies are available
     * @post Spring Boot application starts and initializes the console interface
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        SpringApplication.run(TssCalendarApplication.class, args);
    }
    @Autowired
    private ConfigurableApplicationContext context;
    @Autowired
    private Firestore firestoreService;
    @Autowired
    private AiClient aiClient;
    @Autowired
    private FirestoreController firestoreController;

    /**
     * Creates and returns a CommandLineRunner that provides the interactive console menu.
     * 
     * @pre All required services (Firestore, AI, Controllers) are properly initialized
     * @post Interactive console menu is displayed and user can manage events
     * @param firestoreService The Firestore service for event management
     * @return CommandLineRunner that handles the console interface
     */
    @Bean
    public CommandLineRunner run(Firestore firestoreService) {
        return args -> {
            Scanner input = new Scanner(System.in);
            while (true) {
                System.out.print("    EVENT MANAGEMENT SYSTEM - BACKEND CONSOLE\n" +
                        "    Spring Boot Backend Server Running on Port 8080\n" +
                        "════════════════════════════════════════════════════════\n" +
                        "\n" +
                        "[1] Fetch All Events From Google Form\n" +
                        "[2] View Pending Events List\n" +
                        "[3] Approve Event by Event Title\n" +
                        "[4] Decline Event by Event Title\n" +
                        "[5] View All Approved Events (Firestore)\n" +
                        "[6] Send Email\n" +
                        "[7] Activate AI\n" +
                        "[0] Exit\n" +
                        "\n" +
                        "Enter your choice: ");
                int choice = input.nextInt();
                input.nextLine(); // Consume newline
                
                if (choice == 1) {
                    firestoreController.addToFirestore();
                } else if (choice == 2) {
                    List<String> pendingEvents = firestoreService.getAllEventsStatusBased("pending");
                    System.out.println("\n========== PENDING EVENTS ==========\n");
                    if (pendingEvents.isEmpty()) {
                        System.out.println("No pending events found.");
                    } else {
                        for (String event : pendingEvents) {
                            System.out.println(event);
                            System.out.println("--------------------------------\n");
                        }
                    }
                    System.out.println("Total pending events: " + pendingEvents.size());
                } else if (choice == 3) {
                    System.out.print("Please type in the name of the TITLE OF THE EVENT with no spelling mistakes to approve: ");
                    String eventTitle = input.nextLine();
                    firestoreService.approveEvent(eventTitle);
                } else if (choice == 4) {
                    System.out.print("Please type in the name of the TITLE OF THE EVENT with no spelling mistakes to decline: ");
                    String eventTitle = input.nextLine();
                    firestoreService.declineEvent(eventTitle);
                } else if (choice == 5) {
                    List<String> approvedEvents = firestoreService.getAllEventsStatusBased("approved");
                    System.out.println("\n========== APPROVED EVENTS ==========\n");
                    if (approvedEvents.isEmpty()) {
                        System.out.println("No approved events found.");
                    } else {
                        for (String event : approvedEvents) {
                            System.out.println(event);
                            System.out.println("--------------------------------\n");
                        }
                    }
                    System.out.println("Total approved events: " + approvedEvents.size());
                } else if (choice == 6) {
                    System.out.println("option not available right now");
                } else if (choice == 7) {
                    aiClient.getAiResponse();
                } else if (choice == 0) {
                    int exitCode = SpringApplication.exit(context, () -> 0);
                    System.exit(exitCode); // Terminates the JVM
                } else {
                    System.out.println("Invalid choice. Please try again.");
                }
                
                System.out.println(); // Add spacing before next menu
            }
        };
    }
}