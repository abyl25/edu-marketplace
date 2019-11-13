package com.seniorproject.educationplatform.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Data
public class CourseRequirement {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

//    @JsonIgnoreProperties("courseRequirements")
    @JsonIgnore
    @ManyToOne
    @JoinColumn
    private Course course;

    private String name;

}
