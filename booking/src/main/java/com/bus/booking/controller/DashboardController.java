package com.bus.booking.controller;

import com.bus.booking.DTO.BookingsDTO;
import com.bus.booking.DTO.ReservationDTO;
import com.bus.booking.helper.ObjectCreationHelper;
import com.bus.booking.model.Bookings;
import com.bus.booking.model.BusData;
import com.bus.booking.model.User;
import com.bus.booking.repository.BookingsRepository;
import com.bus.booking.repository.BusDataRepository;
import com.bus.booking.repository.UserRepository;
import com.bus.booking.service.DefaultUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/dashboard")
@Tag(name = "Dashboard Controller", description = "Controller for managing dashboard operations")
public class DashboardController {

    private final DefaultUserService defaultUserService;

    @Autowired
    public DashboardController(DefaultUserService defaultUserService) {
        this.defaultUserService = defaultUserService;
    }

    @Autowired
    BookingsRepository bookingsRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BusDataRepository busDataRepository;

    @ModelAttribute("reservation")
    public ReservationDTO reservationDTO() {
        return new ReservationDTO();
    }

    @GetMapping
    @Operation(summary = "Display Dashboard", description = "Displays the user dashboard with user details")
    public String displayDashboard(Model model) {
        String user = returnUsername();
        model.addAttribute("userDetails", user);

        if (!model.containsAttribute("reservation")) {
            model.addAttribute("reservation", new ReservationDTO());
        }

        return "dashboard";
    }



    @PostMapping
    @Operation(summary = "Filter Bus Data", description = "Filters bus data based on reservation details")
    public String filterBusData(@ModelAttribute("reservation") ReservationDTO reservationDTO, Model model) {
        System.out.println("Received filter data:");
        System.out.println("From: " + reservationDTO.getFrom());
        System.out.println("To: " + reservationDTO.getTo());
        System.out.println("Date: " + reservationDTO.getFilterDate());

        List<BusData> busData = busDataRepository.findByToFromAndDate(
                reservationDTO.getTo(),
                reservationDTO.getFrom(),
                reservationDTO.getFilterDate()
        );

        if (busData.isEmpty()) {
            busData = null;
        }

        String user = returnUsername();
        model.addAttribute("userDetails", user);
        model.addAttribute("busData", busData);
        model.addAttribute("reservation", reservationDTO);

        return "dashboard";
    }

    @GetMapping("/book/{id}")
    @Operation(summary = "Book Page", description = "Displays the booking page for a specific bus")
    public String bookPage(@PathVariable int id, Model model) {
        Optional<BusData> busdata = busDataRepository.findById(id);
        BookingsDTO bks = ObjectCreationHelper.createBookingsDTO(busdata.get());

        String user = returnUsername();
        model.addAttribute("userDetails", user);

        model.addAttribute("record", bks);
        return "book";
    }

    @PostMapping("/booking")
    public String finalBooking(@ModelAttribute("record") BookingsDTO bookingDTO, Model model) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        UserDetails user = (UserDetails) securityContext.getAuthentication().getPrincipal();
        Bookings b = defaultUserService.updateBookings(bookingDTO, user);
        model.addAttribute("record", new BookingsDTO());
        return "redirect:/myBooking";
    }

    private String returnUsername() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        UserDetails user = (UserDetails) securityContext.getAuthentication().getPrincipal();
        User users = userRepository.findByEmail(user.getUsername());
        return users.getName();
    }

    @GetMapping("/myBooking")
    public String myBookings(Model model) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        UserDetails user = (UserDetails) securityContext.getAuthentication().getPrincipal();
        User currentUser = userRepository.findByEmail(user.getUsername());

        List<Bookings> userBookings = bookingsRepository.findByUserId(currentUser.getId());
        model.addAttribute("bookings", userBookings);
        model.addAttribute("userDetails", currentUser.getName());

        return "myBookings";
    }


}
