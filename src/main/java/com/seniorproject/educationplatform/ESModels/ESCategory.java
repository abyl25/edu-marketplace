package com.seniorproject.educationplatform.ESModels;

import lombok.Data;

import javax.persistence.*;

@Data
public class ESCategory {
    @Id
    private Long id;

    private String name;

}
