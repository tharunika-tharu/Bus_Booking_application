package com.bus.booking.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        String redirectUrl = null;

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority grantedAuthority : authorities) {
            System.out.println("Role: " + grantedAuthority.getAuthority());
            if (grantedAuthority.getAuthority().equals("USER")) {
                redirectUrl = "/dashboard";
                break;
            } else if (grantedAuthority.getAuthority().equals("ADMIN")) {
                redirectUrl = "/adminScreen";
                break;
            }
        }
        if (redirectUrl == null) {
            throw new IllegalStateException("No matching role found for user");
        }
        new DefaultRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}

