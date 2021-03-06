package com.seniorproject.educationplatform.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Category implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="CATEGORY_SEQ")
    @SequenceGenerator(name="CATEGORY_SEQ", sequenceName = "CATEGORY_SEQ", allocationSize = 1)
    private Long id;

    private String name;

//    @JsonIgnoreProperties("parent")
//    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
//    private List<Category> subcategories = new ArrayList<>();

//    @JsonIgnoreProperties("subcategories")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_id")
    private Category parent;

    public Category() {}

    public Category(String name) {
        this.name = name;
    }

    public Category(String name, Category category) {
        this.name = name;
        this.parent = category;
    }

//    @JsonIgnoreProperties("subcategory")
//    @OneToMany(mappedBy = "subcategory", fetch = FetchType.LAZY)
//    private List<Topic> topics = new ArrayList<>();

//    @JsonIgnoreProperties("category")
//    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
//    private List<Course> courses = new ArrayList<>();

}
