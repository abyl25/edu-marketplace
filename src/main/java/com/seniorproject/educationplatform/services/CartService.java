package com.seniorproject.educationplatform.services;

import com.seniorproject.educationplatform.models.Cart;
import com.seniorproject.educationplatform.models.CartItem;
import com.seniorproject.educationplatform.models.Course;
import com.seniorproject.educationplatform.repositories.CartItemRepo;
import com.seniorproject.educationplatform.repositories.CartRepo;
import com.seniorproject.educationplatform.repositories.CourseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CartService {
//    private CourseService courseService;
    private CourseRepo courseRepo;
    private CartRepo cartRepo;
    private CartItemRepo cartItemRepo;

    @Autowired
    public CartService(CourseRepo courseRepo, CartRepo cartRepo, CartItemRepo cartItemRepo) {
        this.courseRepo = courseRepo;
        this.cartRepo = cartRepo;
        this.cartItemRepo = cartItemRepo;
    }

    public List<CartItem> getCoursesInCart(Long cartId) {
        return cartItemRepo.findByCartId(cartId);
    }

    public Cart getCartById(Long id) {
        return cartRepo.findById(id).orElse(null);
    }

    public ResponseEntity<Object> addCourseToCart(Long userId, Long courseId) {
        Cart cart = getCartById(userId);
        Course courseToAdd = courseRepo.findById(courseId).get();
        List<CartItem> cartItems = cart.getCartItems();
        for (CartItem cartItem : cartItems) {
            if (cartItem.getCourse().getId().equals(courseToAdd.getId())) {
                return ResponseEntity.unprocessableEntity().body(courseToAdd.getTitle() + " already added");
            }
        }
        CartItem newCartItem = new CartItem();
        newCartItem.setCourse(courseToAdd);
        newCartItem.setCart(cart);
        cartItems.add(newCartItem);
        newCartItem = cartItemRepo.save(newCartItem);
        return ResponseEntity.ok(newCartItem);
    }

    public ResponseEntity<Object> removeCourseFromCart(Long userId, Long courseId) {
        Long count = cartItemRepo.removeByCartIdAndCourseId(userId, courseId);
        return ResponseEntity.ok("Course removed, count: " + count);
    }

    public void emptyCart(Long userId) {
        Cart cart = getCartById(userId);
        List<CartItem> cartItems = cart.getCartItems();
        cartItemRepo.deleteAll(cartItems);
//        return "OK";
    }

}
