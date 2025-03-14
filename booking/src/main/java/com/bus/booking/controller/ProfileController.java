package com.bus.booking.controller;

import com.bus.booking.DTO.ProfileDTO;
import com.bus.booking.model.User;
import com.bus.booking.repository.UserRepository;
import com.bus.booking.service.DefaultUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private DefaultUserService userService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String showProfilePage(Model model) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByEmail(userDetails.getUsername());

        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setName(user.getName());
        profileDTO.setEmail(user.getEmail());

        model.addAttribute("profileDTO", profileDTO);
        return "profile";
    }

    @PostMapping
    public String updateProfile(@ModelAttribute("profileDTO") ProfileDTO profileDTO, Model model) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        try {
            userService.updateProfile(profileDTO, userDetails);
            model.addAttribute("success", "Profile updated successfully!");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }

        return "profile";
    }
}