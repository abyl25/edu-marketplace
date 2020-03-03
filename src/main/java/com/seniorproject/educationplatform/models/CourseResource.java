package com.seniorproject.educationplatform.models;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class CourseResource {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="RESOURCE_SEQ")
    @SequenceGenerator(name="RESOURCE_SEQ", sequenceName = "RESOURCE_SEQ", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn
    private CourseLecture courseLecture;

    private String name;

    private String link; // ?

}
