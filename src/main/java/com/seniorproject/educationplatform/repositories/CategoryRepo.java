package com.seniorproject.educationplatform.repositories;

import com.seniorproject.educationplatform.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepo extends JpaRepository<Category, Long> {
    boolean existsByNameIgnoreCase(String categoryName);

    List<Category> findByParentId(Long id);

    Category findByNameIgnoreCase(String categoryName);

}
