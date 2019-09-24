//package com.seniorproject.educationplatform.models;
//
//import lombok.Data;
//
//import javax.persistence.Entity;
//import javax.persistence.FetchType;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.OneToMany;
//import javax.persistence.OneToOne;
//import java.util.HashSet;
//import java.util.Set;
//
//@Entity
//@Data
//public class Student extends User {
////    @Id
////    @GeneratedValue(strategy = GenerationType.AUTO)
////    private Long id;
////
////    private String firstName;
////
////    private String lastName;
////
////    private String email;
////
////    private String password;
////
////    private String imageName;
//
//    @OneToMany(mappedBy = "student", fetch = FetchType.EAGER)
//    private Set<CourseOrder> courseOrders = new HashSet<>();
//
//    @OneToOne(mappedBy = "student", fetch = FetchType.EAGER)
//    private Cart cart;
//
//    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
//    private Set<Review> reviews = new HashSet<>();
//
//    @OneToMany(mappedBy = "student", fetch = FetchType.EAGER)
//    private Set<LectureQA> questions = new HashSet<>();
//
//}
