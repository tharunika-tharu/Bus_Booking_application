package com.bus.booking.service;

import com.bus.booking.DTO.BookingsDTO;
import com.bus.booking.DTO.ProfileDTO;
import com.bus.booking.DTO.UserRegisteredDTO;
import com.bus.booking.model.Bookings;
import com.bus.booking.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Transactional;

public interface DefaultUserService extends UserDetailsService {

    User save(UserRegisteredDTO userRegisteredDTO);
    Bookings updateBookings(BookingsDTO bookingDTO, UserDetails user);

    @Transactional
    void updateProfile(ProfileDTO profileDTO, UserDetails userDetails);
}