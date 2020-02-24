package com.seniorproject.educationplatform.models;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
public class CourseLecture {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn
    private CourseSection courseSection;

    private String name;

    private String videoLink;

//    private float length; // ??? length of video lecture

    private boolean completed = false;

//    @OneToMany(mappedBy = "courseLecture", fetch = FetchType.EAGER)
//    private Set<CourseResource> courseResources = new HashSet<>();

//    @OneToMany(mappedBy = "courseLecture", fetch = FetchType.EAGER)
//    private Set<LectureQA> lectureQAs = new HashSet<>();

    public CourseLecture() {}

    public CourseLecture(String name, CourseSection courseSection) {
        this.name = name;
        this.courseSection = courseSection;
    }

}
