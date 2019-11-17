package com.seniorproject.educationplatform.repositories;

import com.seniorproject.educationplatform.models.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepo extends JpaRepository<CartItem, Long> {
    List<CartItem> findByCartId(Long cartId);

    Long removeByCartIdAndCourseId(Long cartId, Long courseId);
}
