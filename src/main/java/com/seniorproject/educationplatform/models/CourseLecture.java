package com.seniorproject.educationplatform.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class CourseLecture implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="LECTURE_SEQ")
    @SequenceGenerator(name="LECTURE_SEQ", sequenceName = "LECTURE_SEQ", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn
    private CourseSection courseSection;

    private String name;

    @OneToOne(mappedBy = "courseLecture", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Video video;

    @JsonIgnoreProperties("courseLecture")
    @OneToMany(mappedBy = "courseLecture", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id asc")
    private List<CourseFile> files = new ArrayList<>();

//    @JsonIgnoreProperties("courseLecture")
//    @OneToMany(mappedBy = "courseLecture")
//    private List<Comment> comments = new ArrayList<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "completedLectures")
    private List<User> students = new ArrayList<>();

    public CourseLecture() {}

    public CourseLecture(String name, CourseSection courseSection) {
        this.name = name;
        this.courseSection = courseSection;
    }

}
