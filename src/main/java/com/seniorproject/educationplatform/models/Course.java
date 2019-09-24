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
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    private String subtitle;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "instructor_id")
    private User instructor;   // FK
    // former: private Instructor instructor

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

    @OneToMany(mappedBy = "course", fetch = FetchType.EAGER)
    private Set<CourseSection> courseSections = new HashSet<>();

    @OneToMany(mappedBy = "course", fetch = FetchType.EAGER)
    private Set<Review> reviews = new HashSet<>();

    @OneToMany(mappedBy = "course", fetch = FetchType.EAGER)
    private Set<CourseOrder> courseOrders = new HashSet<>();

    @OneToMany(mappedBy = "course", fetch = FetchType.EAGER)
    private Set<CourseGoal> courseGoals = new HashSet<>();

    @OneToMany(mappedBy = "course", fetch = FetchType.EAGER)
    private Set<CourseRequirement> courseRequirements = new HashSet<>();

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
