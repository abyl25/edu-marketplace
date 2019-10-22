package com.seniorproject.educationplatform.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="COURSE_SEQ")
    @SequenceGenerator(name="COURSE_SEQ", sequenceName = "COURSE_SEQ", allocationSize = 1)
    private Long id;

    private String title;

    private String subtitle;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private Level level;

    private String language;

    private String caption; // CC, array

    // rating ?

    private long price;  // price_off ?

    private Date addedDate;

    private Date lastUpdate;

    private String image;

    private String permaLink;

//    private boolean featuredCourse;

    @JsonIgnoreProperties({"password", "roles", "status", "enabled"})
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "instructor_id")
    private User instructor;  // FK

    // @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIgnoreProperties("course")
    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    private List<CourseGoal> courseGoals = new ArrayList<>();

    @JsonIgnoreProperties("course")
    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    private List<CourseRequirement> courseRequirements = new ArrayList<>();

    @JsonIgnoreProperties("parent")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Category category;

    @JsonIgnoreProperties("subcategory")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Topic topic;

//    @JsonIgnoreProperties("course")
//    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
//    private List<CourseSection> courseSections = new ArrayList<>();

//    @JsonIgnoreProperties("course")
//    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
//    private List<Review> reviews = new ArrayList<>();

//    @JsonIgnoreProperties("course")
//    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
//    private List<CourseOrder> courseOrders = new ArrayList<>();

}
