package com.fifth.controller;

import com.fifth.entity.User;
import com.fifth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminController {
    @Autowired
    private UserService userService;

    @GetMapping("/usr")
    public String userList(Model model) {
        model.addAttribute("allUsers", userService.allUsers());
        return "usr";
    }

    @PostMapping("/usr")
    public String  deleteUser(@RequestParam(required = true, defaultValue = "" ) Long userId,
                              @RequestParam(required = true, defaultValue = "" ) String action,
                              Model model) {
        if (action.equals("delete")){
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User currUser = userService.findUserById(userId);
            if (currUser.getUsername().equals(auth.getName())){
                SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
            }
            userService.deleteUser(userId);
        }
        else if (action.equals("block")){
            User currUser = userService.findUserById(userId);
            userService.blockUser(currUser);
        }
        else if (action.equals("unblock")){
            User currUser = userService.findUserById(userId);
            userService.unblockUser(currUser);
        }
        return "redirect:/usr";
    }


    @GetMapping("/usr/gt/{userId}")
    public String  gtUser(@PathVariable("userId") Long userId, Model model) {
        model.addAttribute("allUsers", userService.usergtList(userId));
        return "usr";
    }
}