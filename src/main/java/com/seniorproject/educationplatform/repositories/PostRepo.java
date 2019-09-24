package com.seniorproject.educationplatform.repositories;

import com.seniorproject.educationplatform.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepo extends JpaRepository<Post, Long> {

}
