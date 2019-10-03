package com.seniorproject.educationplatform.repositories;

import com.seniorproject.educationplatform.models.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TopicRepo extends JpaRepository<Topic, Long> {
    Topic findByNameIgnoreCase(String topicName);

    List<Topic> findBySubcategoryId(Long categoryId);

    List<Topic> findBySubcategoryNameIgnoreCase(String subCategory);
}
