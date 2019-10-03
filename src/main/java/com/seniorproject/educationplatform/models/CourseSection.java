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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
public class CourseSection {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn
    private Course course; // FK

//    @OneToMany(mappedBy = "courseSection", fetch = FetchType.LAZY)
//    private List<CourseLecture> courseLectures = new ArrayList<>();

//    @OneToMany(mappedBy = "courseSection", fetch = FetchType.LAZY)
//    private List<CourseArticle> courseArticles = new ArrayList<>();

}
