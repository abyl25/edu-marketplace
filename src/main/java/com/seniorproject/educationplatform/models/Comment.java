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

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "prnt_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> children = new ArrayList<>();

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

    private Date editedAt;

    public Comment() {}

    public Comment(String content) {
        this.content = content;
    }
}
