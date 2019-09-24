package com.seniorproject.educationplatform.repositories;

import com.seniorproject.educationplatform.models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepo extends JpaRepository<Cart, Long> {

}
