package com.seniorproject.educationplatform.dto.lecture;

import com.seniorproject.educationplatform.models.Comment;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class GetPostsResDto {
    private Long id;
    private UserDto user;
    private String title;
    private String content;
    private Date date;
    private int helpfulCount;
    private boolean resolved;
    private List<Comment> comments;

}

@Data
class UserDto {
    private String firstName;
    private String lastName;
    private String userName;
}

@Data
class CommentDto {
    private Long id;
    private CommentDto parent;
//    private Post post;
    private String title;
    private String content;
    private Date date;
}
