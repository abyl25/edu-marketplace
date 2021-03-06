package com.seniorproject.educationplatform.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class CourseSection implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="SECTION_SEQ")
    @SequenceGenerator(name="SECTION_SEQ", sequenceName = "SECTION_SEQ", allocationSize = 1)
    private Long id;

    private String name;

    @ToString.Exclude
    @JsonIgnore
    @ManyToOne
    @JoinColumn
    private Course course;

    @JsonIgnoreProperties("courseSection")
    @OneToMany(mappedBy = "courseSection", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id asc")
    private List<CourseLecture> lectures = new ArrayList<>();

//    @OneToMany(mappedBy = "courseSection", fetch = FetchType.LAZY)
//    private List<CourseArticle> courseArticles = new ArrayList<>();

    public CourseSection() {}

    public CourseSection(String name) {
        this.name = name;
    }

    public CourseSection(String name, Course course) {
        this.name = name;
        this.course = course;
    }

}
