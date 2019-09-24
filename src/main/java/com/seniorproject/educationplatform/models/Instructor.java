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
//import java.util.HashSet;
//import java.util.Set;
//
//@Entity
//@Data
//public class Instructor extends User {
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
//    @OneToMany(mappedBy = "instructor", fetch = FetchType.LAZY)
//    private Set<Course> courses = new HashSet<>();
//
//    private String headline;
//
//    private String biography;
//
//    private String language;
//
//    private String linkedin;
//
//    private String facebook;
//
//    private String twitter;
//
//    private String youtube;
//
//    private String website;
//
//}
