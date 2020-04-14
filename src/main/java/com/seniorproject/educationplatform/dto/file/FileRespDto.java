package com.seniorproject.educationplatform.dto.file;

import lombok.Data;

import java.io.Serializable;

@Data
public class FileRespDto implements Serializable {
    private Long id;
    private Long lectureId;
    private String fileName;
    private String fileFormat;
    private String videoThumbnail;
    private String type;
}
