package com.bus.booking.service;

import com.bus.booking.DTO.BookingsDTO;
import com.bus.booking.DTO.ProfileDTO;
import com.bus.booking.DTO.UserRegisteredDTO;
import com.bus.booking.model.Bookings;
import com.bus.booking.model.User;
import com.bus.booking.repository.BookingsRepository;
import com.bus.booking.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@Primary
public class DefaultUserServiceImpl implements DefaultUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingsRepository bookingsRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User save(UserRegisteredDTO userRegisteredDTO) {
        User user = new User();
        user.setEmail(userRegisteredDTO.getEmail_id().toLowerCase());
        user.setName(userRegisteredDTO.getName());

        String rawPassword = userRegisteredDTO.getPassword();
        String hashedPassword = passwordEncoder.encode(rawPassword);

        System.out.println("Storing User: " + userRegisteredDTO.getEmail_id());
        System.out.println("Raw Password: " + rawPassword);
        System.out.println("Hashed Password: " + hashedPassword);

        user.setPassword(hashedPassword);
        User savedUser = userRepository.save(user);
        System.out.println("User Successfully Saved to Database: " + savedUser.getEmail());

        return savedUser;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("Login Attempt for email: '" + email + "'");

        User user = userRepository.findByEmail(email);

        if (user == null) {
            System.out.println(" User not found for email: " + email);
            throw new UsernameNotFoundException("Invalid username or password.");
        }

        System.out.println("User found: " + user.getEmail());
        System.out.println("Stored Hashed Password: " + user.getPassword());

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(), Collections.emptyList()); // No roles required
    }



    @Override
    @Transactional
    public Bookings updateBookings(BookingsDTO bookingDTO, UserDetails user) {
        User currentUser = userRepository.findByEmail(user.getUsername());

        Bookings booking = new Bookings();
        booking.setUserId(currentUser.getId());
        booking.setFromDestination(bookingDTO.getFromDestination());
        booking.setToDestination(bookingDTO.getToDestination());
        booking.setFilterDate(bookingDTO.getFilterDate());
        booking.setNoOfPersons(bookingDTO.getNoOfPersons());
        booking.setTotalCalculated(bookingDTO.getTotalCalculated());
        booking.setBusName(bookingDTO.getBusName());
        booking.setTime(bookingDTO.getTime());
        booking.setTripStatus(true);

        return bookingsRepository.save(booking);
    }

    @Transactional
    @Override
    public void updateProfile(ProfileDTO profileDTO, UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername());

        if (profileDTO.getName() != null && !profileDTO.getName().isEmpty()) {
            user.setName(profileDTO.getName());
        }

        if (profileDTO.getEmail() != null && !profileDTO.getEmail().isEmpty()) {
            user.setEmail(profileDTO.getEmail());
        }

        if (profileDTO.getNewPassword() != null && !profileDTO.getNewPassword().isEmpty()) {
            if (passwordEncoder.matches(profileDTO.getCurrentPassword(), user.getPassword())) {
                if (profileDTO.getNewPassword().equals(profileDTO.getConfirmPassword())) {
                    user.setPassword(passwordEncoder.encode(profileDTO.getNewPassword()));
                } else {
                    throw new IllegalArgumentException("New password and confirm password do not match.");
                }
            } else {
                throw new IllegalArgumentException("Current password is incorrect.");
            }
        }

        userRepository.save(user);
    }


}
