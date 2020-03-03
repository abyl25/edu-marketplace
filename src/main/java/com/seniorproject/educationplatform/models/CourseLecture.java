package com.seniorproject.educationplatform.models;

import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
public class CourseLecture {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="LECTURE_SEQ")
    @SequenceGenerator(name="LECTURE_SEQ", sequenceName = "LECTURE_SEQ", allocationSize = 1)
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
