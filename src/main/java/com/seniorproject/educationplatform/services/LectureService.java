package com.seniorproject.educationplatform.services;

import com.seniorproject.educationplatform.models.CourseLecture;
import com.seniorproject.educationplatform.models.User;
import com.seniorproject.educationplatform.repositories.CourseLectureRepo;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
public class LectureService {
    @PersistenceContext
    private EntityManager entityManager;
    private CourseLectureRepo courseLectureRepo;
    private UserService userService;

    public LectureService(CourseLectureRepo courseLectureRepo, UserService userService) {
        this.courseLectureRepo = courseLectureRepo;
        this.userService = userService;
    }

    public List getUserCompletedLecturesByCourse(Long userId, Long courseId) {
        return entityManager.createQuery(
             "select completedLecture.id " +
                "from users user " +
                "join user.completedLectures completedLecture " +
                "join completedLecture.courseSection section " +
                "join section.course course " +
                "where course.id = :courseId and user.id = :userId"
            )
            .setParameter("courseId", courseId)
            .setParameter("userId", userId)
            .getResultList();
    }

    public void toggleCompleteLecture(Long lectureId, Long studentId) {
        CourseLecture courseLecture = courseLectureRepo.findById(lectureId).get();
        User student = userService.getUserById(studentId);
        List<CourseLecture> completedLectures = student.getCompletedLectures();
        if (completedLectures.contains(courseLecture)) {
            completedLectures.remove(courseLecture);
        } else {
            completedLectures.add(courseLecture);
        }
        student.setCompletedLectures(completedLectures);
        userService.save(student);
    }

}
