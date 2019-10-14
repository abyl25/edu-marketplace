package com.seniorproject.educationplatform.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Role {
//    User, Student, Instructor, Admin
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="ROLE_SEQ")
    @SequenceGenerator(name="ROLE_SEQ", sequenceName = "ROLE_SEQ", allocationSize = 1)
    private Long id;

    private String name;

    @ToString.Exclude
    @JsonIgnore
    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private List<User> users = new ArrayList<>();

    public Role() {
    }

    public Role(String roleName) {
        this.name = roleName;
    }

}
