package com.tsscalendar.TSS.Calendar;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;

public class EmailSend {
    public static void send(String receiver, String subject, String html) {
        System.out.println(receiver);
        System.out.println(subject);
        System.out.println(html);
        Resend resend = new Resend("re_azL1naPJ_JbxUbJYYWdYjU1VHQRHsQV9S");

        CreateEmailOptions params = CreateEmailOptions.builder()
                .from("TSS-Calendar <calendar@sportiner.com>")
                .to(receiver)
                .subject(subject)
                .html(html)
                .build();

        try {
            CreateEmailResponse data = resend.emails().send(params);
            System.out.println("Email Sent!");
        } catch (ResendException e) {
            e.printStackTrace();
        }
    }
}