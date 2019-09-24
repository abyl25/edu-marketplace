package com.seniorproject.educationplatform.repositories;

import com.seniorproject.educationplatform.models.CourseOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseOrderRepo extends JpaRepository<CourseOrder, Long> {

}
