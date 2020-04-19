package com.seniorproject.educationplatform.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
public class Review implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="REVIEW_SEQ")
    @SequenceGenerator(name="REVIEW_SEQ", sequenceName = "REVIEW_SEQ", allocationSize = 1)
    private Long id;

    @JsonIgnoreProperties("student")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id")
    private User student;

    @JsonIgnoreProperties("course")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    private String content;

    private float rating;

    private Date addedDate;

    private Date editedDate;

    private int helpfulCount = 0;

    private boolean featured = false;

}
