package com.bus.booking.controller;

import com.bus.booking.service.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.context.Context;

@RestController
public class EmailController {

    @Autowired
    private EmailService emailService;

    @GetMapping("/generate-ticket")
    public String generateTicket(
            @RequestParam String email,
            @RequestParam String name,
            @RequestParam String busName,
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam String date,
            @RequestParam String seatNumber) throws MessagingException {


        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("busName", busName);
        context.setVariable("from", from);
        context.setVariable("to", to);
        context.setVariable("date", date);
        context.setVariable("seatNumber", seatNumber);

        emailService.sendHtmlEmail(email, "Your E-Ticket", "template", context);

        return "E-Ticket sent successfully!";
    }
}