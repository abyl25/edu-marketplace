package com.seniorproject.educationplatform.repositories;

import com.seniorproject.educationplatform.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepo extends JpaRepository<Comment, Long> {
//    List<Comment> findByPostId(Long postId);

    List<Comment> findByCourseLectureId(Long lectureId);

//    List<Comment> findByCourseLectureIdAndParent(Long lectureId, Object o);
}
