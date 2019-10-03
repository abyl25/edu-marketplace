package com.seniorproject.educationplatform.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

//    @JsonIgnoreProperties("parent")
//    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
//    private List<Category> subcategories = new ArrayList<>();

//    @JsonIgnoreProperties("subcategories")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_id")
    private Category parent;

//    @JsonIgnoreProperties("subcategory")
//    @OneToMany(mappedBy = "subcategory", fetch = FetchType.LAZY)
//    private List<Topic> topics = new ArrayList<>();

//    @JsonIgnoreProperties("category")
//    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
//    private List<Course> courses = new ArrayList<>();

}
