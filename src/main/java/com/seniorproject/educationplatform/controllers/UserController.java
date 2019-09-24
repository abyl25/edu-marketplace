package com.seniorproject.educationplatform.controllers;

import com.seniorproject.educationplatform.models.Role;
import com.seniorproject.educationplatform.repositories.RoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/user")
public class UserController {
    private RoleRepo roleRepo;

    @Autowired
    public UserController(RoleRepo roleRepo) {
        this.roleRepo = roleRepo;
    }

    @GetMapping(value = "/role/{id}")
    public Role getRole(@PathVariable Long id) {
        System.out.println("hit url: /user/role");
        Role role = null;
        long type = (long) 1;
        System.out.println("type: " + type);
        if (roleRepo.findById(type).isPresent()) {
            System.out.println("role presents");
            role = roleRepo.findById(type).get();
            System.out.println("role: " + role);
        } else {
            System.out.println("role does not present");
        }
        return role;
    }
}
