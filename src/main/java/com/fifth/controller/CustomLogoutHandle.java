package com.fifth.controller;

import com.fifth.entity.User;
import com.fifth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class CustomLogoutHandle implements LogoutHandler {
    @Autowired
    private UserRepository userRepository;

    @Override
    public void logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName());
        user.setStatus("offline");
        userRepository.save(user);
    }
}
