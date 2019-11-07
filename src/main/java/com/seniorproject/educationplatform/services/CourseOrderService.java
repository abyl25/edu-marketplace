package com.seniorproject.educationplatform.services;

import com.seniorproject.educationplatform.dto.course.CourseOrderReqDto;
import com.seniorproject.educationplatform.models.Course;
import com.seniorproject.educationplatform.models.CourseOrder;
import com.seniorproject.educationplatform.models.User;
import com.seniorproject.educationplatform.repositories.CourseOrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CourseOrderService {
    private CourseOrderRepo courseOrderRepo;
    private CourseService courseService;
    private UserService userService;

    @Autowired
    public CourseOrderService(CourseOrderRepo courseOrderRepo, CourseService courseService, UserService userService) {
        this.courseOrderRepo = courseOrderRepo;
        this.courseService = courseService;
        this.userService = userService;
    }

    public void purchaseCourse(CourseOrderReqDto courseOrderReqDto) {
        CourseOrder courseOrder = courseOrderDtoToEntity(courseOrderReqDto);
        courseOrderRepo.save(courseOrder);
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
        User user = userService.getUserById(courseOrderReqDto.getStudentId()).get();
        courseOrder.setStudent(user);
        Course course = courseService.getCourseById(courseOrderReqDto.getCourseId()).get();
        courseOrder.setCourse(course);
        courseOrder.setPrice(courseOrderReqDto.getPrice());
        courseOrder.setOrderDate(new Date());
        // courseOrder.setPaymentType();
        return courseOrder;
    }

}
