package com.seniorproject.educationplatform.dto.file;

import lombok.Data;

import java.io.Serializable;

@Data
public class VideoDto implements Serializable {
    private String name;
    private String path;
    private String extension;
    private String longFormat;
    private String format;
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
}
