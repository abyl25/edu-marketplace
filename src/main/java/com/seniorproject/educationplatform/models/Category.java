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
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="CATEGORY_SEQ")
    @SequenceGenerator(name="CATEGORY_SEQ", sequenceName = "CATEGORY_SEQ", allocationSize = 1)
    private Long id;

    private String name;

//    @JsonIgnoreProperties("parent")
//    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
//    private List<Category> subcategories = new ArrayList<>();

//    @JsonIgnoreProperties("subcategories")
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
