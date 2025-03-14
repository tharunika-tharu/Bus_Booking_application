package com.bus.booking.controller;

import com.bus.booking.DTO.UserRegisteredDTO;
import com.bus.booking.service.DefaultUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/registration")
@Tag(name = "Registration Controller", description = "Controller for managing user registration")
public class RegistrationController {

    private DefaultUserService userService;

    public RegistrationController(DefaultUserService userService) {
        super();
        this.userService = userService;
    }

    @ModelAttribute("user")
    public UserRegisteredDTO userRegistrationDto() {
        return new UserRegisteredDTO();
    }

    @GetMapping
    @Operation(summary = "Show Registration Form", description = "Displays the registration form")
    public String showRegistrationPage() {
        return "register";
    }

    @PostMapping
    @Operation(summary = "Register User Account", description = "Registers a new user account and redirects to the login page")
    public String registerUserAccount(@ModelAttribute UserRegisteredDTO userRegisteredDTO, Model model) {
        System.out.println("Registration Request: " + userRegisteredDTO.getEmail_id());

        try {
            userService.save(userRegisteredDTO);
            System.out.println("User Registered Successfully: " + userRegisteredDTO.getEmail_id());
            return "redirect:/login?registered=true";
        } catch (Exception e) {
            System.out.println("Registration Failed: " + e.getMessage());
            model.addAttribute("error", "Registration failed. Try again.");
            return "register";
        }
    }
}
