package com.seniorproject.educationplatform.repositories;

import com.seniorproject.educationplatform.models.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnouncementRepo extends JpaRepository<Announcement, Long> {

}
