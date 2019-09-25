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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    private String subtitle;

    private String description;

    @Enumerated(EnumType.STRING)
    private Level level;

    private String language;

    private String caption; // CC, array

    // rating ?

    private Long price;  // price_off ?

    private Date addedDate;

    private Date lastUpdate;

    private String image;

    private String permaLink;

//    private boolean featuredCourse;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "instructor_id")
    private User instructor;  // FK

    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    private List<CourseSection> courseSections = new ArrayList<>();

    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    private List<CourseOrder> courseOrders = new ArrayList<>();

    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    private List<CourseGoal> courseGoals = new ArrayList<>();

    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    private List<CourseRequirement> courseRequirements = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Topic topic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Cart cart;  // ???

}
