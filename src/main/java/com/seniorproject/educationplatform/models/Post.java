package com.seniorproject.educationplatform.models;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn
    private User instructor; // User ?
    // former: Instructor instructor;

    private String title;

    private String content;

    private Timestamp postedTime;

}
