package com.seniorproject.educationplatform.models;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

@Entity
@Data
public class CourseFile {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="RESOURCE_SEQ")
    @SequenceGenerator(name="RESOURCE_SEQ", sequenceName = "RESOURCE_SEQ", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn
    private CourseLecture courseLecture;

    private String name;

    private String fileName;

    private String filePath;

    private String fileFormat;

}
