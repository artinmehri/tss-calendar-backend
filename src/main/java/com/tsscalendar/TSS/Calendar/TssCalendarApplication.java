package com.tsscalendar.TSS.Calendar;
import com.tsscalendar.TSS.Calendar.controller.FirestoreController;
import com.tsscalendar.TSS.Calendar.service.Firestore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Scanner;
import java.util.List;

@SpringBootApplication
public class TssCalendarApplication {
    public static void main(String[] args) {
        SpringApplication.run(TssCalendarApplication.class, args);
    }
    @Autowired
    private ConfigurableApplicationContext context;
    @Autowired
    private Firestore firestoreService;
    @Autowired
    private FirestoreController firestoreController;

    public EmailSend emailSend;

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
                    EmailSend.main();
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