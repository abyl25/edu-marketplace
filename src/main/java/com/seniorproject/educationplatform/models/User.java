package com.seniorproject.educationplatform.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="USERS_SEQ")
    @SequenceGenerator(name="USERS_SEQ", sequenceName = "USERS_SEQ", allocationSize = 1)
    private Long id;

    private String firstName;

    private String lastName;

    private String userName;

    private String email;

    private String password;

    private String imageName;

//    @JsonIgnoreProperties("users")
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(name = "user_roles", joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")}, inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    private List<Role> roles = new ArrayList<>();
//    private int userType;

    @Enumerated(EnumType.STRING)
    private Status status;

    private boolean enabled = false;

    // Student Relationships
    @JsonIgnore
    @OneToOne(mappedBy = "student", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Cart cart;

//    @JsonIgnoreProperties("student")
//    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    private List<CourseOrder> courseOrders = new ArrayList<>();

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
