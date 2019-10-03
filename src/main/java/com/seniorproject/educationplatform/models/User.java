package com.seniorproject.educationplatform.models;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String firstName;

    private String lastName;

    private String userName;

    private String email;

    private String password;

    private String imageName;

//    @JsonIgnoreProperties("users")
    @ManyToMany(fetch = FetchType.LAZY) // cascade = CascadeType.ALL
    @JoinTable(name = "user_roles", joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")}, inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    private List<Role> roles = new ArrayList<>();
//    private int userType;

    @Enumerated(EnumType.STRING)
    private Status status;

    private boolean enabled = false;


    // Student Relationships
//    @JsonIgnoreProperties("student")
//    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    private List<CourseOrder> courseOrders = new ArrayList<>();

//    @JsonIgnoreProperties("student")
//    @OneToOne(mappedBy = "student", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    private Cart cart;

//    @JsonIgnoreProperties("student")
//    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    private List<Review> reviews = new ArrayList<>();

//    @JsonIgnoreProperties("student")
//    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    private List<LectureQA> questions = new ArrayList<>();


    // Instructor Relationships
//    @JsonIgnoreProperties("instructor")
//    @OneToMany(mappedBy = "instructor", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    private List<Course> courses = new ArrayList<>();

//    @OneToOne(mappedBy = "instructor", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    private InstructorDetails instructorDetails;

}
