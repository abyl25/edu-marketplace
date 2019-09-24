//package com.seniorproject.educationplatform.services;
//
//import com.seniorproject.educationplatform.models.Student;
//import com.seniorproject.educationplatform.repositories.StudentRepo;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class StudentService {
//    private StudentRepo studentRepo;
//
//    @Autowired
//    public StudentService(StudentRepo studentRepo) {
//        this.studentRepo = studentRepo;
//    }
//
//    public Student save(Student student) {
//        return studentRepo.save(student);
//    }
//
//    public List<Student> getStudents() {
//        return studentRepo.findAll();
//    }
//
//    public Optional<Student> getStudentById(Long id) {
//        return studentRepo.findById(id);
//    }
//
//    public void delete(Student student) {
//        studentRepo.delete(student);
//    }
//
//    public Student findByUserName(String userName) {
//        return studentRepo.findByUserName(userName);
//    }
//
//    public boolean existsByUserName(String userName) {
//        return studentRepo.existsByUserName(userName);
//    }
//
//}
