package com.seniorproject.educationplatform.models;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

@Entity
@Data
public class InstructorDetails {
    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private User instructor;

    // Instructor fields
    private String headline;

    private String biography;

    private String language;

    private String linkedin;

    private String facebook;

    private String twitter;

    private String youtube;

    private String website;

}
