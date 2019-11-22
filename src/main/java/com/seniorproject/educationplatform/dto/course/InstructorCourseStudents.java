package com.seniorproject.educationplatform.dto.course;

import java.sql.Date;

public interface InstructorCourseStudents {
    String getFirstName();

    String getLastName();

    String getEmail();

    Long getCourseId();

    String getCourseTitle();

    String getInstructorFirstName();

    String getInstructorLastName();

    Date getOrderDate();
}
