package com.seniorproject.educationplatform.models.ESModels;

import lombok.Data;

import javax.persistence.*;

@Data
public class ESCategory {
    @Id
    private Long id;

    private String name;

}
