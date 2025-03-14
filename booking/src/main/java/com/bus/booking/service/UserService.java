package com.bus.booking.service;

import com.bus.booking.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User saveUser(String email, String password);
}
