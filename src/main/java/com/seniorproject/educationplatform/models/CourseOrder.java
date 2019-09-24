package com.seniorproject.educationplatform.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;

@Entity
@Data
@IdClass(CourseOrder.CourseOrderId.class)
public class CourseOrder implements Serializable {
    @Id
    @ManyToOne
    @JoinColumn
    private User student; // Student student

    @Id
    @ManyToOne
    @JoinColumn
    private Course course;

    private long price;

    // price_off, receipt ?

    private Date orderDate;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;


    @EqualsAndHashCode
    public static class CourseOrderId implements Serializable {
        private User student;
        private Course course;

        public CourseOrderId() {}

        public CourseOrderId(User student, Course course) {
            this.student = student;
            this.course = course;
        }

    }

}
