package com.seniorproject.educationplatform.services;

import com.seniorproject.educationplatform.dto.course.CourseOrderReqDto;
import com.seniorproject.educationplatform.models.Course;
import com.seniorproject.educationplatform.models.CourseOrder;
import com.seniorproject.educationplatform.models.CourseOrderKey;
import com.seniorproject.educationplatform.models.User;
import com.seniorproject.educationplatform.repositories.CourseOrderRepo;
import com.seniorproject.educationplatform.repositories.CourseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseOrderService {
    private CourseOrderRepo courseOrderRepo;
    private CourseRepo courseRepo;
    private UserService userService;

    @Autowired
    public CourseOrderService(CourseOrderRepo courseOrderRepo, CourseRepo courseRepo, UserService userService) {
        this.courseOrderRepo = courseOrderRepo;
        this.courseRepo = courseRepo;
        this.userService = userService;
    }

    public List<Course> getRegisteredCourses(Long userId) {
        List<CourseOrder> courseOrders = getOrdersByStudentId(userId);
        return courseOrders.stream().map(CourseOrder::getCourse).collect(Collectors.toList());
    }

    public ResponseEntity getRegisteredCourse(Long userId, Long courseId) {
        boolean purchased = checkIfStudentPurchasedCourse(userId, courseId);
        if (!purchased) {
            return ResponseEntity.unprocessableEntity().body("Student already purchased this course");
        }
        Course course = courseRepo.findById(courseId).get();
        return ResponseEntity.ok(course);
    }

    public ResponseEntity purchaseCourse(CourseOrderReqDto courseOrderReqDto) {
        boolean alreadyPurchased = checkIfStudentPurchasedCourse(courseOrderReqDto.getStudentId(), courseOrderReqDto.getCourseId());
        if (alreadyPurchased) {
            return ResponseEntity.unprocessableEntity().body("Student already purchased this course");
        }
        CourseOrder courseOrder = courseOrderDtoToEntity(courseOrderReqDto);
        courseOrderRepo.save(courseOrder);
        return ResponseEntity.ok("Course registered!");
    }

    public void dropFromCourse(Long userId, Long courseId) {
        courseOrderRepo.removeByStudentIdAndCourseId(userId, courseId);
    }

    public List<CourseOrder> getOrdersByStudentId(Long studentId) {
        return courseOrderRepo.findByStudentId(studentId);
    }

    public List<CourseOrder> getOrdersByCourseId(Long courseId) {
        return courseOrderRepo.findByCourseId(courseId);
    }

    public boolean checkIfStudentPurchasedCourse(Long studentId, Long courseId) {
        return courseOrderRepo.existsByStudentIdAndCourseId(studentId, courseId);
    }

    // Mappers
    private CourseOrder courseOrderDtoToEntity(CourseOrderReqDto courseOrderReqDto) {
        CourseOrder courseOrder = new CourseOrder();
        courseOrder.setId(new CourseOrderKey(courseOrderReqDto.getStudentId(), courseOrderReqDto.getCourseId()));
        User user = userService.getUserById(courseOrderReqDto.getStudentId()).get();
        courseOrder.setStudent(user);
        Course course = courseRepo.findById(courseOrderReqDto.getCourseId()).get();
        courseOrder.setCourse(course);
        courseOrder.setPrice(courseOrderReqDto.getPrice());
        courseOrder.setOrderDate(new Date());
        // courseOrder.setPaymentType();
        return courseOrder;
    }

}
