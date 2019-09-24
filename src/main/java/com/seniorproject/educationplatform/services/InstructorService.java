//package com.seniorproject.educationplatform.services;
//
//import com.seniorproject.educationplatform.models.Instructor;
//import com.seniorproject.educationplatform.repositories.InstructorRepo;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class InstructorService {
//    private InstructorRepo instructorRepo;
//
//    @Autowired
//    public InstructorService(InstructorRepo instructorRepo) {
//        this.instructorRepo = instructorRepo;
//    }
//
//    public Instructor save(Instructor instructor) {
//        return instructorRepo.save(instructor);
//    }
//
//    public List<Instructor> getInstructors() {
//        return instructorRepo.findAll();
//    }
//
//    public Optional<Instructor> getInstructorById(Long id) {
//        return instructorRepo.findById(id);
//    }
//
//    public void delete(Instructor instructor) {
//        instructorRepo.delete(instructor);
//    }
//
//    public Instructor findByUserName(String userName) {
//        return instructorRepo.findByUserName(userName);
//    }
//
//    public boolean existsByUserName(String userName) {
//        return instructorRepo.existsByUserName(userName);
//    }
//
//}
