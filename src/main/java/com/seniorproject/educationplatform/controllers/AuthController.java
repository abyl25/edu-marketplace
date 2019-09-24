package com.seniorproject.educationplatform.controllers;

import com.seniorproject.educationplatform.dto.LoginRequestDto;
import com.seniorproject.educationplatform.dto.SignUpRequestDto;
import com.seniorproject.educationplatform.models.User;
import com.seniorproject.educationplatform.services.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/auth")
@Slf4j
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequestDto requestDto) {
        log.info("AuthController, login() ");
        String userName = requestDto.getUserName();
        String token = authService.login(requestDto);
        Map<Object, Object> response = new HashMap<>();
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
        String token = authService.signUp(requestDto);
        Map<Object, Object> response = new HashMap<>();
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
