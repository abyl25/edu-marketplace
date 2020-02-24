package com.seniorproject.educationplatform.repositories;

import com.seniorproject.educationplatform.dto.course.InstructorCourseStudents;
import com.seniorproject.educationplatform.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepo extends JpaRepository<User, Long> {
    boolean existsByUserName(String username);

    User findByUserName(String userName);

    User findByEmail(String email);

    @Query(nativeQuery = true, value =
        "SELECT u.first_name as firstName, u.last_name as lastName, u.email as email, c.id as courseId, c.title as courseTitle, " +
        "instructor.first_name as instructorFirstName, instructor.last_name as instructorLastName, co.order_date as orderDate " +
        "FROM users u " +
        "JOIN course_order co ON u.id = co.student_id " +
        "JOIN course c ON c.id = co.course_id " +
        "JOIN users instructor ON c.instructor_id = instructor.id  " +
        "WHERE c.id = :course_id")
    List<InstructorCourseStudents> getInstructorStudents(@Param("course_id") Long courseId); // @Param("instr_id") Long instructorId,

    @Query(nativeQuery = true, value =
        "SELECT COUNT(*)" +
        "FROM users u " +
        "JOIN course_order co ON u.id = co.student_id " +
        "JOIN course c ON c.id = co.course_id " +
        "JOIN users instructor ON c.instructor_id = instructor.id " +
        "WHERE instructor.id = :instructor_id")
    int getInstructorStudentsCount(@Param("instructor_id") Long instructorId);

//    @Query(value = "Select u.firstName, u.lastName from user u join courseOrder.student co")
//    List<?> getInstructorStudentsJpql( @Param("course_id") Long courseId);

}
