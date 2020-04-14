package com.seniorproject.educationplatform.repositories;

import com.seniorproject.educationplatform.models.Video;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepo extends JpaRepository<Video, Long> {
}
