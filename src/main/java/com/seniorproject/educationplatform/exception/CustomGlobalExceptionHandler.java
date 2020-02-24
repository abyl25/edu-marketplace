package com.seniorproject.educationplatform.exception;

import com.seniorproject.educationplatform.security.JwtAuthenticationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, Map> body = new HashMap<>();
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        body.put("errors", errors);
        return new ResponseEntity<>(body, headers, status);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    protected ResponseEntity<Object> handleUsernameNotFoundException(Exception ex, WebRequest request) {
        log.info("LOG: login: Username not found");
        return ResponseEntity.status(400).body("Username not found");
    }

    @ExceptionHandler(DisabledException.class)
    protected ResponseEntity<Object> handleDisabledException(Exception ex, WebRequest request) {
        log.info("LOG: login: User with username is disabled");
        return ResponseEntity.status(422).body("You haven't confirmed your account yet. Please, do it by an email we have sent you");
    }

    @ExceptionHandler(BadCredentialsException.class)
    protected ResponseEntity<Object> handleBadCredentialsException(Exception ex, WebRequest request) {
        log.info("LOG: login: Invalid username or password");
        return ResponseEntity.status(422).body("Invalid username or password");
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    protected ResponseEntity<Object> handleUsernameAlreadyExistsException(Exception ex, WebRequest request) {
        log.info("LOG: signup: Username is already in use");
        return ResponseEntity.status(422).body(ex.getMessage());
    }

    @ExceptionHandler(CustomException.class) // NotInstructorCourseException
    protected ResponseEntity<Object> CustomException(Exception ex, WebRequest request) {
        log.info("LOG: CustomException: " + ex.getMessage());
        return ResponseEntity.status(422).body(ex.getMessage());
    }

//    @ExceptionHandler(JwtAuthenticationException.class)
//    protected ResponseEntity<Object> handleJwtExpiredException(Exception ex, WebRequest request) {
//        log.info("LOG: auth: JWT token is expired or invalid");
//        log.info("LOG: ex msg: " + ex.getMessage());
//        return ResponseEntity.status(401).body(ex.getMessage());
//    }

}
