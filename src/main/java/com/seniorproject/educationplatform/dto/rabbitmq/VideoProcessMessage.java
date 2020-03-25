package com.seniorproject.educationplatform.dto.rabbitmq;

import lombok.Data;

import java.io.Serializable;

@Data
public class VideoProcessMessage implements Serializable {
    private String videoName;
    private String path;
    private String extension;
    private String format;
    private int width;
    private int height;
    private int frames;
    private String videoCodec;

}
