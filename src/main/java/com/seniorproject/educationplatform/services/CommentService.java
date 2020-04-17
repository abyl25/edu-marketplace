package com.seniorproject.educationplatform.services;

import com.seniorproject.educationplatform.dto.lecture.AddCommentReqDto;
import com.seniorproject.educationplatform.models.Comment;
import com.seniorproject.educationplatform.models.CourseLecture;
import com.seniorproject.educationplatform.models.User;
import com.seniorproject.educationplatform.repositories.CommentRepo;
import com.seniorproject.educationplatform.repositories.CourseLectureRepo;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CommentService {
    private CommentRepo commentRepo;
    private CourseLectureRepo courseLectureRepo;
    private UserService userService;
    private ModelMapper modelMapper;

    public CommentService(CommentRepo commentRepo, CourseLectureRepo courseLectureRepo, UserService userService, ModelMapper modelMapper) {
        this.commentRepo = commentRepo;
        this.courseLectureRepo = courseLectureRepo;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    public List<Comment> getCommentsByLecture(Long lectureId) {
        return commentRepo.findByCourseLectureId(lectureId);
    }

//    public List<Comment> getRootCommentsByLecture(Long lectureId) {
//        return commentRepo.findByCourseLectureIdAndParent(lectureId, null);
//    }

    public Comment addComment(Long lectureId, AddCommentReqDto addCommentReqDto) {
        Comment comment = new Comment(addCommentReqDto.getContent());
        if (addCommentReqDto.getParentId() != null) {
//            Comment parentComment = commentRepo.findById(addCommentReqDto.getParentId()).get();
//            comment.setParent(parentComment);
            comment.setParentId(addCommentReqDto.getParentId());
        }
        CourseLecture lecture = courseLectureRepo.findById(lectureId).get();
        comment.setCourseLecture(lecture);
        User user = userService.getUserById(addCommentReqDto.getUserId());
        comment.setUser(user);
        comment.setDate(new Date());
        return commentRepo.save(comment);
    }

}
