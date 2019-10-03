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
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

//    @JsonIgnoreProperties("topics")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subcategory_id")
    private Category subcategory; // parent FK

    private String name;

//    @JsonIgnoreProperties("topic")
//    @OneToMany(mappedBy = "topic", fetch = FetchType.LAZY)
//    private List<Course> courses = new ArrayList<>();

}
