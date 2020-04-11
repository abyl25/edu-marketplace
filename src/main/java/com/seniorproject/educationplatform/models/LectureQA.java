package com.seniorproject.educationplatform.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Data
public class LectureQA implements Serializable {
    // save as Tree, use Nested Set Model?

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // parent_id // ???

    @JsonIgnoreProperties("courseLecture")
    @ManyToOne
    @JoinColumn
    private CourseLecture courseLecture;

    @JsonIgnoreProperties("student")
    @ManyToOne
    @JoinColumn
    private User student;
    // former: Student student;

    private String title;

    private String content;

    private Timestamp askedTime; // Date ???

    private int helpfulCount;

    private boolean resolved = false;

}
