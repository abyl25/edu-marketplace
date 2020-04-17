package com.seniorproject.educationplatform.dto.lecture;

import lombok.Data;

@Data
public class AddCommentReqDto {
    private Long parentId;
    private Long userId;
    private String title;
    private String content;
}
