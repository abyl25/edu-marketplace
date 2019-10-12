package com.seniorproject.educationplatform.controllers;

import com.seniorproject.educationplatform.models.Cart;
import com.seniorproject.educationplatform.models.CartItem;
import com.seniorproject.educationplatform.security.JwtUser;
import com.seniorproject.educationplatform.services.AuthService;
import com.seniorproject.educationplatform.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/cart")
public class CartController {
    private CartService cartService;
    private AuthService authService;

    @Autowired
    public CartController(CartService cartService, AuthService authService) {
        this.cartService = cartService;
        this.authService = authService;
    }

    @GetMapping
    public ResponseEntity getCoursesInCart() {
        JwtUser jwtUser = (JwtUser) authService.getLoggedInUser();
        Cart cart = jwtUser.getCart();
        List<CartItem> cartItems = cartService.getCoursesInCart(cart.getId());
        return ResponseEntity.ok(cartItems);
    }

    @PostMapping("/{courseId}")
    public ResponseEntity addCourseToCart(@PathVariable Long courseId) {
        JwtUser jwtUser = (JwtUser) authService.getLoggedInUser();
        System.out.println("jwtUser: " + jwtUser);
        Cart cart = jwtUser.getCart();
        System.out.println("user cart id: " + cart.getId());
        return cartService.addCourseToCart(cart, courseId);
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity removeCourseFromCart(@PathVariable Long courseId) {
        return cartService.removeCourseFromCart(courseId);
    }

}
