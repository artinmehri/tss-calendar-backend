/**
 * EmailSend.java
 *
 * Utility class for sending emails using Resend API.
 * Provides static method to send HTML emails.
 *
 * Sources:
 * - Resend Java Documentation: https://resend.com/java
 *
 * @author Artin Mehri
 * @version 1.0
 */
package com.tsscalendar.TSS.Calendar;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;

public class EmailSend {
    public static void main() {
        Resend resend = new Resend("re_azL1naPJ_JbxUbJYYWdYjU1VHQRHsQV9S");

        CreateEmailOptions params = CreateEmailOptions.builder()
                .from("TSS-Calendar <calendar@sportiner.com>")
                .to("artinmehri7777@gmail.com")
                .subject("App Club Event Reminder!")
                .html("<!DOCTYPE html>\n" +
                        "<html lang=\"en\">\n" +
                        "<head>\n" +
                        "    <meta charset=\"UTF-8\">\n" +
                        "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                        "    <title>App Club Meeting Reminder</title>\n" +
                        "    <style>\n" +
                        "        /* General Reset */\n" +
                        "        body {\n" +
                        "            margin: 0;\n" +
                        "            padding: 0;\n" +
                        "            background-color: #FDF2E3; /* Cream background from mockup */\n" +
                        "            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;\n" +
                        "            color: #333333;\n" +
                        "        }\n" +
                        "\n" +
                        "        .email-container {\n" +
                        "            max-width: 600px;\n" +
                        "            margin: 20px auto;\n" +
                        "            background-color: #ffffff;\n" +
                        "            border-radius: 12px;\n" +
                        "            overflow: hidden;\n" +
                        "            box-shadow: 0 4px 15px rgba(0,0,0,0.05);\n" +
                        "        }\n" +
                        "\n" +
                        "        /* Header Section */\n" +
                        "        .header {\n" +
                        "            background-color: #4A3F6D; /* Deep purple from mockup */\n" +
                        "            color: #ffffff;\n" +
                        "            padding: 40px 20px;\n" +
                        "            text-align: center;\n" +
                        "        }\n" +
                        "\n" +
                        "        .header h1 {\n" +
                        "            margin: 0;\n" +
                        "            font-size: 28px;\n" +
                        "            letter-spacing: 1px;\n" +
                        "            text-transform: uppercase;\n" +
                        "        }\n" +
                        "\n" +
                        "        /* Hero / Quote Section */\n" +
                        "        .quote-box {\n" +
                        "            background-color: #E8B4C3; /* Soft pink from mockup */\n" +
                        "            padding: 20px;\n" +
                        "            text-align: center;\n" +
                        "            font-style: italic;\n" +
                        "            color: #4A3F6D;\n" +
                        "            font-weight: 500;\n" +
                        "        }\n" +
                        "\n" +
                        "        /* Content Body */\n" +
                        "        .content {\n" +
                        "            padding: 30px;\n" +
                        "            line-height: 1.6;\n" +
                        "        }\n" +
                        "\n" +
                        "        .highlight-text {\n" +
                        "            color: #4A3F6D;\n" +
                        "            font-weight: bold;\n" +
                        "            font-size: 18px;\n" +
                        "        }\n" +
                        "\n" +
                        "        .feature-list {\n" +
                        "            list-style: none;\n" +
                        "            padding: 0;\n" +
                        "        }\n" +
                        "\n" +
                        "        .feature-item {\n" +
                        "            background: #f9f9f9;\n" +
                        "            margin-bottom: 10px;\n" +
                        "            padding: 12px;\n" +
                        "            border-radius: 8px;\n" +
                        "            border-left: 4px solid #E8B4C3;\n" +
                        "        }\n" +
                        "\n" +
                        "        /* Meeting Details Card */\n" +
                        "        .details-card {\n" +
                        "            background-color: #FDF2E3;\n" +
                        "            border: 2px dashed #4A3F6D;\n" +
                        "            border-radius: 12px;\n" +
                        "            padding: 20px;\n" +
                        "            margin-top: 25px;\n" +
                        "            text-align: center;\n" +
                        "        }\n" +
                        "\n" +
                        "        .details-card h2 {\n" +
                        "            margin-top: 0;\n" +
                        "            color: #4A3F6D;\n" +
                        "        }\n" +
                        "\n" +
                        "        /* CTA Button */\n" +
                        "        .btn {\n" +
                        "            display: inline-block;\n" +
                        "            background-color: #4A3F6D;\n" +
                        "            color: #ffffff !important;\n" +
                        "            text-decoration: none;\n" +
                        "            padding: 15px 30px;\n" +
                        "            border-radius: 25px;\n" +
                        "            font-weight: bold;\n" +
                        "            margin-top: 20px;\n" +
                        "            transition: opacity 0.3s;\n" +
                        "        }\n" +
                        "\n" +
                        "        /* Footer */\n" +
                        "        .footer {\n" +
                        "            background-color: #f4f4f4;\n" +
                        "            text-align: center;\n" +
                        "            padding: 20px;\n" +
                        "            font-size: 12px;\n" +
                        "            color: #888888;\n" +
                        "        }\n" +
                        "\n" +
                        "        .social-link {\n" +
                        "            color: #4A3F6D;\n" +
                        "            text-decoration: none;\n" +
                        "            font-weight: bold;\n" +
                        "        }\n" +
                        "    </style>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "\n" +
                        "    <div class=\"email-container\">\n" +
                        "        <div class=\"header\">\n" +
                        "            <h1>APP CLUB</h1>\n" +
                        "        </div>\n" +
                        "\n" +
                        "        <div class=\"quote-box\">\n" +
                        "            \"Ever wished Thornhill had one place to see ALL upcoming school events? We’re building it.\"\n" +
                        "        </div>\n" +
                        "\n" +
                        "        <div class=\"content\">\n" +
                        "            <p class=\"highlight-text\">Come build something real — together.</p>\n" +
                        "            <p>App Club is developing a school-wide digital calendar app. We need <strong>designers, coders, and creative thinkers</strong> to bring this to life. No experience? No problem.</p>\n" +
                        "            \n" +
                        "            <ul class=\"feature-list\">\n" +
                        "                <li class=\"feature-item\">\uD83D\uDE80 <strong>View & Filter:</strong> See sports, clubs, and spirit days in one view.</li>\n" +
                        "                <li class=\"feature-item\">\uD83D\uDEE0\uFE0F <strong>Real Experience:</strong> Perfect for SHSM, Uni apps, and your portfolio.</li>\n" +
                        "                <li class=\"feature-item\">\uD83E\uDD1D <strong>Community:</strong> Build a tool the entire school will actually use.</li>\n" +
                        "            </ul>\n" +
                        "\n" +
                        "            <div class=\"details-card\">\n" +
                        "                <h2>Next Meeting</h2>\n" +
                        "                <p><strong>\uD83D\uDCCD Where:</strong> Room 225<br>\n" +
                        "                <strong>\uD83D\uDD52 When:</strong> Monday After School</p>\n" +
                        "                \n" +
                        "                <p><strong>Join the Google Classroom:</strong><br>\n" +
                        "                <code style=\"font-size: 20px; color: #4A3F6D; background: #fff; padding: 5px 10px; border-radius: 4px;\">n5tvcop5</code></p>\n" +
                        "                \n" +
                        "                <a href=\"#\" class=\"btn\">Join the Project</a>\n" +
                        "            </div>\n" +
                        "        </div>\n" +
                        "\n" +
                        "        <div class=\"footer\">\n" +
                        "            <p>Follow us on Instagram <a href=\"https://instagram.com/appclub.tss\" class=\"social-link\">@appclub.tss</a></p>\n" +
                        "            <p>Thornhill Secondary School | App Club 2026</p>\n" +
                        "        </div>\n" +
                        "    </div>\n" +
                        "\n" +
                        "</body>\n" +
                        "</html>")
                .build();

        try {
            CreateEmailResponse data = resend.emails().send(params);
            System.out.println("Email Sent!");
        } catch (ResendException e) {
            e.printStackTrace();
        }
    }
}