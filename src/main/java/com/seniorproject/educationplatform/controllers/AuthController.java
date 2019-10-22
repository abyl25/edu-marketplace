package com.seniorproject.educationplatform.controllers;

import com.seniorproject.educationplatform.dto.auth.LoginRequestDto;
import com.seniorproject.educationplatform.dto.auth.PasswordUpdateDto;
import com.seniorproject.educationplatform.dto.auth.SignUpRequestDto;
import com.seniorproject.educationplatform.exception.CustomException;
import com.seniorproject.educationplatform.models.User;
import com.seniorproject.educationplatform.security.JwtUser;
import com.seniorproject.educationplatform.services.AuthService;
import com.seniorproject.educationplatform.services.UserService;
import com.seniorproject.educationplatform.services.VerificationTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/auth")
@Slf4j
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
    public ResponseEntity login(@Valid @RequestBody LoginRequestDto requestDto) {
        log.info("AuthController, login() ");
        String userName = requestDto.getUserName();
        String token;
        try {
            token = authService.login(requestDto);
        } catch (UsernameNotFoundException e) {
            log.info("LOG: AuthController login: Username " + userName + " not found");
            return ResponseEntity.status(400).body("Username " + userName + " not found");
        } catch (DisabledException e) {
            log.info("LOG: AuthController login: User with username: {} is disabled", userName);
            return ResponseEntity.status(422).body("You haven't confirmed your account yet. Please, do it by an email we have sent you");
        }
        catch (BadCredentialsException e) {
            log.info("LOG: AuthController login: Invalid username or password");
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
    public ResponseEntity signUp(@Valid @RequestBody SignUpRequestDto requestDto) {
        log.info("AuthController, signup() ");
        System.out.println("req body: " + requestDto);
        String userName = requestDto.getUserName();
        try {
            authService.signUp(requestDto);
        } catch (CustomException e) {
            return ResponseEntity.status(422).body("Username is already in use");
        }
//        User user = userService.findByUserName(userName);
        Map<Object, Object> response = new HashMap<>();
//        response.put("user", user);
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
        return (JwtUser) authService.getLoggedInUser();
    }

    @GetMapping("/me")
    public ResponseEntity me(HttpServletRequest req) {
        log.info("AuthController, me() ");
        User me = authService.me(req);
        return ResponseEntity.ok(me);
    }

}
