package com.seniorproject.educationplatform.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
public class Video implements Serializable {
    @Id
    private Long id;

    @JsonIgnore
    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    private CourseLecture courseLecture;

    private String name;
    private String path;
    private String extension;
    private String fullName;
    private String longFormat;
    private String format;
    private String thumbnail;
    private double duration;
    private long size;
    private int width;
    private int height;
    private String displayAspectRatio;
    private int frames;
    private long videoBitrate;
    private long audioBitrate;
    private int audioChannel;
    private String videoCodec;
    private String videoCodecLong;

    public Video() {}

    public Video(String name, String path, String extension) {
        this.name = name;
        this.path = path;
        this.extension = extension;
    }

    public Video(String name, String path, String extension, String thumbnail, long duration) {
        this.name = name;
        this.path = path;
        this.extension = extension;
        this.thumbnail = thumbnail;
        this.duration = duration;
    }
}
