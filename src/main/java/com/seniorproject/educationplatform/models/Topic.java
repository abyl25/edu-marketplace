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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="TOPIC_SEQ")
    @SequenceGenerator(name="TOPIC_SEQ", sequenceName = "TOPIC_SEQ", allocationSize = 1)
    private Long id;

//    @JsonIgnoreProperties("topics")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subcategory_id")
    private Category subcategory; // parent FK

    private String name;

    public Topic() {}

    public Topic(String name, Category subcategory) {
        this.name = name;
        this.subcategory = subcategory;
    }

//    @JsonIgnoreProperties("topic")
//    @OneToMany(mappedBy = "topic", fetch = FetchType.LAZY)
//    private List<Course> courses = new ArrayList<>();

}
