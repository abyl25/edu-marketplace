package com.seniorproject.educationplatform.controllers;

import com.seniorproject.educationplatform.dto.auth.LoginRequestDto;
import com.seniorproject.educationplatform.dto.auth.PasswordUpdateDto;
import com.seniorproject.educationplatform.dto.auth.SignUpRequestDto;
import com.seniorproject.educationplatform.models.User;
import com.seniorproject.educationplatform.security.JwtUser;
import com.seniorproject.educationplatform.services.AuthService;
import com.seniorproject.educationplatform.services.UserService;
import com.seniorproject.educationplatform.services.VerificationTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/api/auth")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;
    private final VerificationTokenService verificationTokenService;

    @Autowired
    public AuthController(AuthService authService, UserService userService, VerificationTokenService verificationTokenService) {
        this.authService = authService;
        this.userService = userService;
        this.verificationTokenService = verificationTokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@Valid @RequestBody LoginRequestDto requestDto) {
        log.info("AuthController, login() ");
        String userName = requestDto.getUserName();
        String token = authService.login(requestDto);
        User user = userService.findByUserName(userName);
        Map<Object, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("user", user);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> signUp(@Valid @RequestBody SignUpRequestDto requestDto) {
        log.info("AuthController, signup() ");
        System.out.println("req body: " + requestDto);
        String userName = requestDto.getUserName();
        authService.signUp(requestDto);
        Map<Object, Object> response = new HashMap<>();
        response.put("userName", userName);
        response.put("message", "We have sent you a confirmation email. Please, confirm your account!");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/confirmAccount")
    public ResponseEntity confirmAccount(@RequestParam("token") String token) {
        return verificationTokenService.verifyAccount(token);
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity forgotPassword(@RequestParam("email") String email) {
        return authService.createPasswordResetToken(email);
    }

    @GetMapping("/resetPassword")
    public ResponseEntity resetPassword(@RequestParam("id") long id, @RequestParam("token") String token) {
        return authService.resetPassword(id, token);
    }

    @PostMapping("/updatePassword")
    public ResponseEntity updatePassword(@Valid @RequestBody PasswordUpdateDto passwordUpdateDto) {
        log.info("LOG: AuthController, updatePassword()");
        return authService.updatePassword(passwordUpdateDto);
    }

    @GetMapping("/user")
    public JwtUser getLoggedInUser(Authentication auth) {
        return authService.getLoggedInUser(); // (JwtUser)
    }

    @GetMapping("/me")
    public ResponseEntity me(HttpServletRequest req) {
        log.info("AuthController, me() ");
        User me = authService.me(req);
        return ResponseEntity.ok(me);
    }

}
