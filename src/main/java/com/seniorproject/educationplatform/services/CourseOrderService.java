package com.seniorproject.educationplatform.services;

import com.seniorproject.educationplatform.dto.course.CourseOrderReqDto;
import com.seniorproject.educationplatform.dto.course.InstructorCourseStudents;
import com.seniorproject.educationplatform.dto.course.MultiCourseOrderReqDto;
import com.seniorproject.educationplatform.models.Course;
import com.seniorproject.educationplatform.models.CourseOrder;
import com.seniorproject.educationplatform.models.CourseOrderKey;
import com.seniorproject.educationplatform.models.User;
import com.seniorproject.educationplatform.repositories.CourseOrderRepo;
import com.seniorproject.educationplatform.repositories.CourseRepo;
import com.seniorproject.educationplatform.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseOrderService {
    private CourseOrderRepo courseOrderRepo;
    private CourseRepo courseRepo;
    private UserService userService;
    private UserRepo userRepo;
    private CartService cartService;

    @Autowired
    public CourseOrderService(CourseOrderRepo courseOrderRepo, CourseRepo courseRepo, UserService userService, UserRepo userRepo, CartService cartService) {
        this.courseOrderRepo = courseOrderRepo;
        this.courseRepo = courseRepo;
        this.userService = userService;
        this.userRepo = userRepo;
        this.cartService = cartService;
    }

    public List<Course> getRegisteredCourses(Long userId) {
        List<CourseOrder> courseOrders = this.getOrdersByStudentId(userId);
        return courseOrders.stream().map(CourseOrder::getCourse).collect(Collectors.toList());
    }

    public ResponseEntity<Object> getRegisteredCourse(Long userId, Long courseId) {
        boolean purchased = this.checkIfStudentPurchasedCourse(userId, courseId);
        if (!purchased) {
            return ResponseEntity.unprocessableEntity().body("Student haven't purchased this course");
        }
        Course course = courseRepo.findById(courseId).get();
        return ResponseEntity.ok(course);
    }

    public ResponseEntity<Object> purchaseCourse(CourseOrderReqDto courseOrderReqDto) {
        boolean alreadyPurchased = checkIfStudentPurchasedCourse(courseOrderReqDto.getStudentId(), courseOrderReqDto.getCourseId());
        if (alreadyPurchased) {
            return ResponseEntity.unprocessableEntity().body("Student already purchased this course");
        }
        CourseOrder courseOrder = courseOrderDtoToEntity(courseOrderReqDto);
        courseOrderRepo.save(courseOrder);
        return ResponseEntity.ok("Course registered!");
    }

    public ResponseEntity<Object> registerToMultipleCourses(MultiCourseOrderReqDto courseOrdersReq) {
        List<Long> courseIds = courseOrdersReq.getCourseIds();
        List<CourseOrder> courseOrders = new ArrayList<>();
        courseIds.forEach(cId -> {
            CourseOrder courseOrder = new CourseOrder();
            Course course = courseRepo.findById(cId).get();
            User student = userRepo.findById(courseOrdersReq.getUserId()).get();
            courseOrder.setId(new CourseOrderKey(student.getId(), course.getId()));
            courseOrder.setStudent(student);
            courseOrder.setCourse(course);
            courseOrder.setPrice(course.getPrice());
            courseOrder.setOrderDate(new Date(System.currentTimeMillis()));
            courseOrders.add(courseOrder);
        });
        courseOrderRepo.saveAll(courseOrders);
        cartService.emptyCart(courseOrdersReq.getUserId());
        return ResponseEntity.ok("Courses registered!");
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

    public ResponseEntity<Object> getInstructorStudentsByCourseId(Long instructorId, Long courseId) {
        Course course = courseRepo.findByIdAndInstructorId(courseId, instructorId);
        if (course == null) {
            return ResponseEntity.unprocessableEntity().body("Instructor does not own this course");
        }
        List<InstructorCourseStudents> instructorCourseStudents = userRepo.getInstructorStudents(courseId);
        System.out.println("LOG: instructorCourseStudents: " + instructorCourseStudents);
        return ResponseEntity.ok(instructorCourseStudents);
    }

    public int getInstructorAllStudentsCount(Long instructorId) {
        return userRepo.getInstructorStudentsCount(instructorId);
    }

//    public List getInstructorStudentsJpql(Long courseId) {
//        return userRepo.getInstructorStudentsJpql(courseId);
//    }

    // Mappers
    private CourseOrder courseOrderDtoToEntity(CourseOrderReqDto courseOrderReqDto) {
        CourseOrder courseOrder = new CourseOrder();
        courseOrder.setId(new CourseOrderKey(courseOrderReqDto.getStudentId(), courseOrderReqDto.getCourseId()));
        User user = userService.getUserById(courseOrderReqDto.getStudentId()).get();
        courseOrder.setStudent(user);
        Course course = courseRepo.findById(courseOrderReqDto.getCourseId()).get();
        courseOrder.setCourse(course);
        courseOrder.setPrice(courseOrderReqDto.getPrice());
        courseOrder.setOrderDate(new Date(System.currentTimeMillis()));
        // courseOrder.setPaymentType();
        return courseOrder;
    }

}
