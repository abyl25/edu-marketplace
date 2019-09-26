package com.seniorproject.educationplatform.controllers;

import com.seniorproject.educationplatform.dto.LoginRequestDto;
import com.seniorproject.educationplatform.dto.SignUpRequestDto;
import com.seniorproject.educationplatform.exception.CustomException;
import com.seniorproject.educationplatform.models.User;
import com.seniorproject.educationplatform.services.AuthService;
import com.seniorproject.educationplatform.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/auth")
@Slf4j
public class AuthController {
    private final AuthService authService;
    private final UserService userService;
    @Autowired
    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequestDto requestDto) {
        log.info("AuthController, login() ");
        String userName = requestDto.getUserName();
        String token = "";
        try {
            token = authService.login(requestDto);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(400).body("User with username:" + userName + " not found");
        } catch (CustomException e) {
            return ResponseEntity.status(422).body("Invalid username or password");
        }
//        User user = userService.findByUserName(userName);
        Map<Object, Object> response = new HashMap<>();
//        response.put("user", user);
        response.put("userName", userName);
        response.put("token", token);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/signup")
    public ResponseEntity signUp(@RequestBody SignUpRequestDto requestDto) {
        log.info("AuthController, signup() ");
        System.out.println("req body: ");
        System.out.println(requestDto);
        String userName = requestDto.getUserName();
        String token = "";
        try {
            token = authService.signUp(requestDto);
        } catch (CustomException e) {
            return ResponseEntity.status(422).body("Username is already in use");
        }
//        User user = userService.findByUserName(userName);
        Map<Object, Object> response = new HashMap<>();
//        response.put("user", user);
        response.put("userName", userName);
        response.put("token", token);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user")
    public void getLoggedInUser(Authentication auth) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Object credentials = auth.getCredentials();
        Object authorities = auth.getAuthorities();
        
    }

    @GetMapping("/me")
    public ResponseEntity me(HttpServletRequest req) {
        log.info("AuthController, me() ");
        User me = authService.me(req);
        return ResponseEntity.ok(me);
    }

}
