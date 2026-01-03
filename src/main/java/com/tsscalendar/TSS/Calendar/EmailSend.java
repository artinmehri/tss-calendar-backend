package com.tsscalendar.TSS.Calendar;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;

public class EmailSend {
    public static void main(String[] args) {
        Resend resend = new Resend("");

        CreateEmailOptions params = CreateEmailOptions.builder()
                .from("TSS Calendar <tsscalendar.com>")
                .to("example@gmail.com")
                .subject("Your Event Reminder!")
                .html("<strong>Hello There!</strong>")
                .build();

        try {
            CreateEmailResponse data = resend.emails().send(params);
            System.out.println(data.getId());
        } catch (ResendException e) {
            e.printStackTrace();
        }
    }
}