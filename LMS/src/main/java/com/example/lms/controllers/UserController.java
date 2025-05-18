package com.example.lms.controllers;

import com.example.lms.models.AuthentictationResponse;
import com.example.lms.models.Profile;
import com.example.lms.models.User;
import com.example.lms.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {


    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/register")
    public ResponseEntity<AuthentictationResponse> register(@RequestBody User user) {
        return ResponseEntity.ok(userService.register(user)); // Register method in service
    }

    @PostMapping("/login")
    public ResponseEntity<AuthentictationResponse> login(@RequestBody User user) {
        return ResponseEntity.ok(userService.login(user)); // Login method in service
    }


    @PostMapping("/editprofile")
    public ResponseEntity<Object> editProfile(@RequestBody Profile userProfile){
        return ResponseEntity.ok(userService.editProfile(userProfile));
    }


    @GetMapping("/profiles/{userId}")
    public ResponseEntity<Object> viewProfile(@PathVariable Integer userId) {
        return ResponseEntity.ok(userService.viewProfile(userId));
    }
}
