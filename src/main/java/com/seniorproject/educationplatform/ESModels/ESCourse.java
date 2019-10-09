package com.seniorproject.educationplatform.ESModels;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.sql.Date;

@Data
@Document(indexName = "course", type = "course")
public class ESCourse {
    @Id
    private Long id;

    private String title;

    private String subtitle;

    private String description;

    private String level;

    private String language;

    private String caption;

    private long price;

    private Date addedDate;

    private Date lastUpdate;

    private String image;

    private String permaLink;

//    private boolean featuredCourse;

    private ESUser instructor;

    private String category;

    private String topic;

}

