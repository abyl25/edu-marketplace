package com.seniorproject.educationplatform.services;

import com.seniorproject.educationplatform.models.Cart;
import com.seniorproject.educationplatform.models.CartItem;
import com.seniorproject.educationplatform.models.Course;
import com.seniorproject.educationplatform.repositories.CartItemRepo;
import com.seniorproject.educationplatform.repositories.CartRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {
    private CourseService courseService;
    private CartRepo cartRepo;
    private CartItemRepo cartItemRepo;

    @Autowired
    public CartService(CourseService courseService, CartRepo cartRepo, CartItemRepo cartItemRepo) {
        this.courseService = courseService;
        this.cartRepo = cartRepo;
        this.cartItemRepo = cartItemRepo;
    }

    public List<CartItem> getCoursesInCart(Long cartId) {
        return cartItemRepo.findByCartId(cartId);
    }

    public Cart getCartById(Long id) {
        return cartRepo.findById(id).orElse(null);
    }

    public ResponseEntity addCourseToCart(Cart cart, Long courseId) {
        Course courseToAdd = courseService.getCourseById(courseId).get();
        List<CartItem> cartItems = cart.getCartItem();
        for (CartItem cartItem : cartItems) {
            if (cartItem.getCourse().getId().equals(courseToAdd.getId())) {
                return ResponseEntity.unprocessableEntity().body(courseToAdd.getTitle() + " already added");
            }
        }

        CartItem newCartItem = new CartItem();
        newCartItem.setCourse(courseToAdd);
        newCartItem.setCart(cart);
        cartItems.add(newCartItem);
        cartItemRepo.save(newCartItem);
        return ResponseEntity.ok("Added " + courseToAdd.getTitle() + " to cart");
    }

    public ResponseEntity removeCourseFromCart(Long courseId) {
        cartItemRepo.deleteById(courseId);
        return ResponseEntity.ok("Course removed");
    }

}
