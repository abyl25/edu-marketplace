package com.seniorproject.educationplatform.repositories;

import com.seniorproject.educationplatform.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepo extends JpaRepository<Category, Long> {

}
