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
public class CourseSection {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="SECTION_SEQ")
    @SequenceGenerator(name="SECTION_SEQ", sequenceName = "SECTION_SEQ", allocationSize = 1)
    private Long id;

    private String name;

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
