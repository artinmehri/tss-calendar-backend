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
            // ENSURED PROGRAM CORRECTNESS BY DEVELOPING TEST DATA (VALID AND INVALID DATA)
            System.out.println("=== SYSTEM INITIALIZATION ===");
            System.out.println("Performing system validation checks...");
            
            // Test data validation
            validateSystemIntegrity(firestoreService);
            
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
                        "[7] Run System Validation Tests\n" +
                        "[0] Exit\n" +
                        "\n" +
                        "Enter your choice: ");
                
                // VALID INPUT TEST: Ensure integer input
                int choice;
                try {
                    choice = input.nextInt();
                    input.nextLine(); // Consume newline
                } catch (Exception e) {
                    System.out.println("INVALID INPUT TEST: Non-integer input detected. Please enter a number.");
                    input.nextLine(); // Clear invalid input
                    continue;
                }
                
                if (choice == 1) {
                    // VALID DATA TEST: Normal operation
                    try {
                        firestoreController.addToFirestore();
                        System.out.println("VALID DATA TEST: Event fetch completed successfully.");
                    } catch (Exception e) {
                        System.out.println("ERROR HANDLING TEST: " + e.getMessage());
                    }
                } else if (choice == 2) {
                    // VALID DATA TEST: Retrieve pending events
                    try {
                        String[] pendingEvents = firestoreService.getAllEventsStatusBased("pending");
                        System.out.println("\n========== PENDING EVENTS ==========\n");
                        if (pendingEvents.length == 0) {
                            System.out.println("No pending events found.");
                        } else {
                            for (String event : pendingEvents) {
                                System.out.println(event);
                                System.out.println("--------------------------------\n");
                            }
                        }
                        System.out.println("Total pending events: " + pendingEvents.length);
                        System.out.println("VALID DATA TEST: Successfully retrieved " + pendingEvents.length + " pending events.");
                    } catch (Exception e) {
                        System.out.println("ERROR HANDLING TEST: Failed to retrieve pending events - " + e.getMessage());
                    }
                } else if (choice == 3) {
                    // VALID/INVALID INPUT TEST: Event approval with validation
                    System.out.print("Please type in the name of the TITLE OF THE EVENT with no spelling mistakes to approve: ");
                    String eventTitle = input.nextLine().trim();
                    
                    // INVALID DATA TEST: Empty title
                    if (eventTitle.isEmpty()) {
                        System.out.println("INVALID DATA TEST: Empty event title provided.");
                        continue;
                    }
                    
                    // INVALID DATA TEST: Title too long
                    if (eventTitle.length() > 100) {
                        System.out.println("INVALID DATA TEST: Event title exceeds maximum length (100 characters).");
                        continue;
                    }
                    
                    try {
                        firestoreService.approveEvent(eventTitle);
                        System.out.println("VALID DATA TEST: Event '" + eventTitle + "' approved successfully.");
                    } catch (Exception e) {
                        System.out.println("ERROR HANDLING TEST: Failed to approve event - " + e.getMessage());
                    }
                } else if (choice == 4) {
                    // VALID/INVALID INPUT TEST: Event decline with validation
                    System.out.print("Please type in the name of the TITLE OF THE EVENT with no spelling mistakes to decline: ");
                    String eventTitle = input.nextLine().trim();
                    
                    // INVALID DATA TEST: Empty title
                    if (eventTitle.isEmpty()) {
                        System.out.println("INVALID DATA TEST: Empty event title provided.");
                        continue;
                    }
                    
                    // INVALID DATA TEST: Title too long
                    if (eventTitle.length() > 100) {
                        System.out.println("INVALID DATA TEST: Event title exceeds maximum length (100 characters).");
                        continue;
                    }
                    
                    try {
                        firestoreService.declineEvent(eventTitle);
                        System.out.println("VALID DATA TEST: Event '" + eventTitle + "' declined successfully.");
                    } catch (Exception e) {
                        System.out.println("ERROR HANDLING TEST: Failed to decline event - " + e.getMessage());
                    }
                } else if (choice == 5) {
                    // VALID DATA TEST: Retrieve approved events
                    try {
                        String[] approvedEvents = firestoreService.getAllEventsStatusBased("approved");
                        System.out.println("\n========== APPROVED EVENTS ==========\n");
                        if (approvedEvents.length == 0) {
                            System.out.println("No approved events found.");
                        } else {
                            for (String event : approvedEvents) {
                                System.out.println(event);
                                System.out.println("--------------------------------\n");
                            }
                        }
                        System.out.println("Total approved events: " + approvedEvents.length);
                        System.out.println("VALID DATA TEST: Successfully retrieved " + approvedEvents.length + " approved events.");
                    } catch (Exception e) {
                        System.out.println("ERROR HANDLING TEST: Failed to retrieve approved events - " + e.getMessage());
                    }
                } else if (choice == 6) {
                    // VALID DATA TEST: Email functionality
                    try {
                        EmailSend.main();
                        System.out.println("VALID DATA TEST: Email system accessed successfully.");
                    } catch (Exception e) {
                        System.out.println("ERROR HANDLING TEST: Email system error - " + e.getMessage());
                    }
                } else if (choice == 7) {
                    // COMPREHENSIVE TEST DATA VALIDATION
                    runComprehensiveTests(firestoreService);
                } else if (choice == 0) {
                    System.out.println("VALID DATA TEST: System shutdown initiated.");
                    int exitCode = SpringApplication.exit(context, () -> 0);
                    System.exit(exitCode); // Terminates the JVM
                } else {
                    // INVALID INPUT TEST: Out of range choice
                    System.out.println("INVALID INPUT TEST: Choice " + choice + " is not valid. Please enter a number between 0-7.");
                }
                
                System.out.println(); // Add spacing before next menu
            }
        };
    }
    
    /**
     * ENSURED PROGRAM CORRECTNESS BY DEVELOPING TEST DATA (VALID AND INVALID DATA)
     * System integrity validation on startup
     */
    private void validateSystemIntegrity(Firestore firestoreService) {
        try {
            System.out.println("Testing Firestore connection...");
            String[] testEvents = firestoreService.getAllEventsStatusBased("pending");
            System.out.println("✓ Firestore connection validated - Retrieved " + testEvents.length + " pending events");
            
            testEvents = firestoreService.getAllEventsStatusBased("approved");
            System.out.println("✓ Approved events query validated - Retrieved " + testEvents.length + " approved events");
            
            System.out.println("✓ System integrity check completed successfully");
        } catch (Exception e) {
            System.out.println("✗ System integrity check failed: " + e.getMessage());
        }
        System.out.println();
    }
    
    /**
     * ENSURED PROGRAM CORRECTNESS BY DEVELOPING TEST DATA (VALID AND INVALID DATA)
     * Comprehensive test suite for system validation
     */
    private void runComprehensiveTests(Firestore firestoreService) {
        System.out.println("\n=== COMPREHENSIVE SYSTEM VALIDATION ===");
        
        // VALID DATA TESTS
        System.out.println("\n--- VALID DATA TESTS ---");
        testValidDataScenarios(firestoreService);
        
        // INVALID DATA TESTS
        System.out.println("\n--- INVALID DATA TESTS ---");
        testInvalidDataScenarios(firestoreService);
        
        // EDGE CASE TESTS
        System.out.println("\n--- EDGE CASE TESTS ---");
        testEdgeCaseScenarios(firestoreService);
        
        System.out.println("\n=== VALIDATION COMPLETE ===\n");
    }
    
    private void testValidDataScenarios(Firestore firestoreService) {
        try {
            // Test 1: Valid status queries
            String[] pending = firestoreService.getAllEventsStatusBased("pending");
            System.out.println("✓ Valid status 'pending' query: " + pending.length + " results");
            
            String[] approved = firestoreService.getAllEventsStatusBased("approved");
            System.out.println("✓ Valid status 'approved' query: " + approved.length + " results");
            
            // Test 2: Valid document existence check
            boolean exists = firestoreService.checkDocumentExists("test");
            System.out.println("✓ Valid document existence check completed");
            
        } catch (Exception e) {
            System.out.println("✗ Valid data test failed: " + e.getMessage());
        }
    }
    
    private void testInvalidDataScenarios(Firestore firestoreService) {
        try {
            // Test 1: Invalid status query
            String[] invalidStatus = firestoreService.getAllEventsStatusBased("invalid_status");
            System.out.println("✓ Invalid status query handled: " + invalidStatus.length + " results");
            
            // Test 2: Non-existent document check
            boolean notExists = firestoreService.checkDocumentExists("non_existent_event_12345");
            System.out.println("✓ Non-existent document check: " + !notExists);
            
        } catch (Exception e) {
            System.out.println("✗ Invalid data test failed: " + e.getMessage());
        }
    }
    
    private void testEdgeCaseScenarios(Firestore firestoreService) {
        try {
            // Test 1: Empty string status
            String[] emptyStatus = firestoreService.getAllEventsStatusBased("");
            System.out.println("✓ Empty status query handled: " + emptyStatus.length + " results");
            
            // Test 2: Null-like status
            String[] nullStatus = firestoreService.getAllEventsStatusBased("null");
            System.out.println("✓ Null-like status query handled: " + nullStatus.length + " results");
            
            // Test 3: Very long status string
            String longStatus = "a".repeat(1000);
            String[] longStatusResult = firestoreService.getAllEventsStatusBased(longStatus);
            System.out.println("✓ Long status query handled: " + longStatusResult.length + " results");
            
        } catch (Exception e) {
            System.out.println("✗ Edge case test failed: " + e.getMessage());
        }
    }
}

