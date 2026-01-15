package com.tsscalendar.TSS.Calendar;

import com.tsscalendar.TSS.Calendar.service.Firestore;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TssCalendarApplication {
    public static void main(String[] args) {
        SpringApplication.run(TssCalendarApplication.class, args);
    }

    @Bean
    public CommandLineRunner run(Firestore firestoreService) {
        return args -> {
            System.out.println("Program started!");
        };
    }
}