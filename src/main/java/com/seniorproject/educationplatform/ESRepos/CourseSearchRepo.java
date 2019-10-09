package com.seniorproject.educationplatform.ESRepos;

import com.seniorproject.educationplatform.ESModels.ESCourse;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface CourseSearchRepo extends ElasticsearchRepository<ESCourse, Long> {
    List<ESCourse> findByTitle(String title);

    List<ESCourse> findByInstructorFirstNameAndInstructorLastName(String firstName, String lastName);

}
