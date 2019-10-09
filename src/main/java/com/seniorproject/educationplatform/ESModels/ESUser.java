package com.seniorproject.educationplatform.ESModels;

import com.seniorproject.educationplatform.models.Role;
import com.seniorproject.educationplatform.models.Status;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
public class ESUser {
    @Id
    private Long id;

    private String firstName;

    private String lastName;

    private String userName;

    private String email;

    private String password;

    private String imageName;

    private List<Role> roles = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Status status;

    private boolean enabled = false;

}
