package com.seniorproject.educationplatform.controllers;

import com.seniorproject.educationplatform.models.CartItem;
import com.seniorproject.educationplatform.services.AuthService;
import com.seniorproject.educationplatform.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class CartController {
    private CartService cartService;
    private AuthService authService;

    @Autowired
    public CartController(CartService cartService, AuthService authService) {
        this.cartService = cartService;
        this.authService = authService;
    }

    @GetMapping("/user/{userId}/cart")
    public ResponseEntity getCoursesInCart(@PathVariable Long userId) {
        List<CartItem> cartItems = cartService.getCoursesInCart(userId);
        return ResponseEntity.ok(cartItems);
    }

    @PostMapping("/user/{userId}/cart/{courseId}")
    public ResponseEntity addCourseToCart(@PathVariable Long userId, @PathVariable Long courseId) {
        return cartService.addCourseToCart(userId, courseId);
    }

    @DeleteMapping("/user/{userId}/cart/{courseId}")
    public ResponseEntity removeCourseFromCart(@PathVariable Long userId, @PathVariable Long courseId) {
        return cartService.removeCourseFromCart(userId, courseId);
    }

}
