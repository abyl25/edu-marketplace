package com.seniorproject.educationplatform.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

    private String videoName;

    private String videoPath;

    private String videoFormat;

//    private float length; // ??? length of video lecture

    private boolean completed = false;

    @JsonIgnoreProperties("courseLecture")
    @OneToMany(mappedBy = "courseLecture", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id asc")
    private List<CourseFile> files = new ArrayList<>();

//    @OneToMany(mappedBy = "courseLecture", fetch = FetchType.EAGER)
//    private Set<LectureQA> lectureQAs = new HashSet<>();

    public CourseLecture() {}

    public CourseLecture(String name, CourseSection courseSection) {
        this.name = name;
        this.courseSection = courseSection;
    }

}
