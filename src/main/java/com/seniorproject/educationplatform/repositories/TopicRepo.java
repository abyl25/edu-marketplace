package com.seniorproject.educationplatform.repositories;

import com.seniorproject.educationplatform.models.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TopicRepo extends JpaRepository<Topic, Long> {
    Topic findByName(String topicName);

    List<Topic> findBySubcategoryId(Long categoryId);

    List<Topic> findBySubcategoryName(String subCategory);
}
