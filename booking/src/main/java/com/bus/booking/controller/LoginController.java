package com.bus.booking.controller;

import com.bus.booking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping
    public String loginForm() {
        return "login";
    }

    @PostMapping("/do-login")
    public String loginUser(@RequestParam("email") String email, @RequestParam("password") String password, Model model) {
        System.out.println("🔹 Received Login Request with Email: '" + email + "'");

        if (email == null || email.trim().isEmpty()) {
            System.out.println("ERROR: Email is NULL or EMPTY in Controller");
            model.addAttribute("error", "Email cannot be empty.");
            return "login";
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            System.out.println("Login Successful for: " + email);
            return "redirect:/dashboard";
        } catch (Exception e) {
            System.out.println(" Login Failed for: " + email);
            model.addAttribute("error", "Invalid email or password.");
            return "login";
        }
    }



}
