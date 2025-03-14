package com.bus.booking.controller;

import com.bus.booking.DTO.BookingsDTO;
import com.bus.booking.helper.ObjectCreationHelper;
import com.bus.booking.model.Bookings;
import com.bus.booking.model.User;
import com.bus.booking.repository.BookingsRepository;
import com.bus.booking.repository.UserRepository;
import com.bus.booking.service.DefaultUserService;
import com.bus.booking.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.context.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/myBooking")
@Tag(name = "My Booking Controller", description = "Controller for managing user bookings")
public class MyBookingController {

    private DefaultUserService userService;

    public MyBookingController(DefaultUserService userService) {
        super();
        this.userService = userService;
    }
    @Autowired
    BookingsRepository bookingsRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    UserRepository userRepository;

    @ModelAttribute("bookings")
    public BookingsDTO bookingDto() {
        return new BookingsDTO();
    }

    @GetMapping
    public String login(Model model) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        UserDetails users = (UserDetails) securityContext.getAuthentication().getPrincipal();
        User user = userRepository.findByEmail(users.getUsername());

        List<BookingsDTO> bks = new ArrayList<>();
        List<Bookings> bs = bookingsRepository.findByUserId(user.getId());
        for (Bookings bookings : bs) {
            System.out.println("Booking ID: " + bookings.getId());
            BookingsDTO bkks = ObjectCreationHelper.createBookingsDTO(bookings);
            bks.add(bkks);
        }
        model.addAttribute("userDetails", user.getName());
        Collections.reverse(bks);
        model.addAttribute("bookings", bks);
        return "myBookings";
    }

    @GetMapping("/generate/{id}")
    @Operation(summary = "Generate Booking", description = "Generates a booking and sends an email")
    public String generateTicket(@PathVariable int id, Model model) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        UserDetails users = (UserDetails) securityContext.getAuthentication().getPrincipal();
        User user = userRepository.findByEmail(users.getUsername());

        // Fetch the booking details
        Optional<Bookings> busdata = bookingsRepository.findById(id);
        if (busdata.isPresent()) {
            Bookings booking = busdata.get();

            try {
                Context context = new Context();
                context.setVariable("name", user.getName());
                context.setVariable("busName", booking.getBusName());
                context.setVariable("time",booking.getTime());
                context.setVariable("from", booking.getFromDestination());
                context.setVariable("to", booking.getToDestination());
                context.setVariable("date", booking.getFilterDate());
                context.setVariable("noOfPersons", booking.getNoOfPersons());

                emailService.sendHtmlEmail(user.getEmail(), "Your E-Ticket", "template", context);

                return "redirect:/myBooking?success=true";
            } catch (MessagingException e) {
                return "redirect:/myBooking?error=true";
            }
        }

        List<BookingsDTO> bks = new ArrayList<>();
        List<Bookings> bs = bookingsRepository.findByUserId(user.getId());
        for (Bookings bookings : bs) {
            BookingsDTO bkks = ObjectCreationHelper.createBookingsDTO(bookings);
            bks.add(bkks);
        }
        model.addAttribute("userDetails", user.getName());
        Collections.reverse(bks);
        model.addAttribute("bookings", bks);

        return "myBookings";
    }

    @GetMapping("/cancel/{id}")
    public String cancelBooking(@PathVariable int id, Model model) {
        Optional<Bookings> busdata = bookingsRepository.findById(id);
        if (busdata.isPresent()) {
            Bookings booking = busdata.get();
            if (booking.isTripStatus()) {
                booking.setTripStatus(false);
                bookingsRepository.save(booking);
                System.out.println("Trip canceled successfully. Redirecting with successCancel=true.");
                return "redirect:/myBooking?successCancel=true";
            }
        }

        System.out.println("Booking not found. Redirecting without parameters.");
        return "redirect:/myBooking";
    }

    private void setData(Bookings busData, Model model) {
        Optional<User> users =userRepository.findById(busData.getUserId());
        List<Bookings> bs = bookingsRepository.findByUserId(users.get().getId());
        Collections.reverse(bs);
        model.addAttribute("bookings",bs);
    }
}
