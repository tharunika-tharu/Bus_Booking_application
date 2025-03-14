package com.bus.booking.service;

import com.bus.booking.DTO.BookingsDTO;
import com.bus.booking.model.Bookings;
import com.bus.booking.model.User;
import com.bus.booking.repository.BookingsRepository;
import com.bus.booking.repository.UserRepository;
import com.bus.booking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    BookingsRepository bookingsRepository;

    @Override
    public User saveUser(String email, String password) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("Invalid email or password.");
        }

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(), Collections.emptyList());
    }

    public Bookings updateBookings(BookingsDTO bookingDTO, UserDetails user) {
        Bookings booking = new Bookings();
        booking.setBusName(bookingDTO.getBusName());
        booking.setTime(bookingDTO.getTime());
        booking.setFromDestination(bookingDTO.getFromDestination());
        booking.setToDestination(bookingDTO.getToDestination());
        booking.setFilterDate(bookingDTO.getFilterDate());
        booking.setNoOfPersons(bookingDTO.getNoOfPersons());
        booking.setTotalCalculated(bookingDTO.getTotalCalculated());
        booking.setTripStatus(bookingDTO.getTripStatus());
        booking.setUserId(userRepository.findByEmail(user.getUsername()).getId());

        return bookingsRepository.save(booking);
    }
}
