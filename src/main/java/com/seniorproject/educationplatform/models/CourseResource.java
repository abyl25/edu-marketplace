package com.seniorproject.educationplatform.models;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class CourseResource {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn
    private CourseLecture courseLecture;

    private String name;

    private String link; // ?

}
