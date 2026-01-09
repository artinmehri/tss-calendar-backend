package com.tsscalendar.TSS.Calendar.controller;
import com.tsscalendar.TSS.Calendar.service.Firestore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/firestore")
public class FirestoreController {

    @Autowired
    private Firestore firestoreService;

    @GetMapping("/add")
    public void addToFirestore() throws ExecutionException, InterruptedException {
         firestoreService.addData();
        }
}