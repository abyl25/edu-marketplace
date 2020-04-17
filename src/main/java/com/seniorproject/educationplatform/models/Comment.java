package com.seniorproject.educationplatform.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class Comment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="COMMENT_SEQ")
    @SequenceGenerator(name="COMMENT_SEQ", sequenceName = "COMMENT_SEQ", allocationSize = 1)
    private Long id;

    private Long parentId;

//    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
//    @ManyToOne
//    @JoinColumn(name = "parent_id")
//    private Comment parent;

////    @JsonIgnoreProperties({"parent"})
//    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
//    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
//    private List<Comment> children = new ArrayList<>();

//    @JsonIgnoreProperties({"courseLecture", "user", "comments"})
//    @ManyToOne
//    @JoinColumn
//    private Post post;

    @JsonIgnore
    @ManyToOne
    @JoinColumn
    private CourseLecture courseLecture;

    @JsonIgnoreProperties({"roles", "password", "enabled", "status", "cart"})
    @ManyToOne
    @JoinColumn
    private User user;

    private String title;

    private String content;

    private Date date;

    public Comment() {}

    public Comment(String content) {
        this.content = content;
    }
}
