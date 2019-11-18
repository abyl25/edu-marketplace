package com.seniorproject.educationplatform.controllers;

import com.seniorproject.educationplatform.models.Role;
import com.seniorproject.educationplatform.models.User;
import com.seniorproject.educationplatform.repositories.RoleRepo;
import com.seniorproject.educationplatform.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class UserController {
    private UserService userService;
    private RoleRepo roleRepo;

    @Autowired
    public UserController(UserService userService, RoleRepo roleRepo) {
        this.userService = userService;
        this.roleRepo = roleRepo;
    }

    @PostMapping("/users")
    public User addUser(User user) {
        return userService.save(user);
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id).get();
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
    }


    /*  Roles Controller  */
    @GetMapping("/roles")
    public ResponseEntity getRoles() {
        List<Role> roles = roleRepo.findAll();
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/role/{roleId}")
    public ResponseEntity getRoleById(@PathVariable Long roleId) {
        Role role = roleRepo.findById(roleId).orElse(null);
        return ResponseEntity.ok(role);
    }

    @PostMapping("/roles/{roleName}")
    public ResponseEntity addRole(@PathVariable String roleName) {
        Role role = new Role(roleName);
        roleRepo.save(role);
        return ResponseEntity.ok("Role added");
    }

    @PostMapping("/roles/{roleNames}")
    public ResponseEntity addRoles(@PathVariable List<String> roleNames) {
        List<Role> roles = roleNames.stream().map(Role::new).collect(Collectors.toList());
        roleRepo.saveAll(roles);
        return ResponseEntity.ok("Roles added");
    }

    @DeleteMapping("/roles/{roleName}")
    public ResponseEntity deleteRole(@PathVariable String roleName) {
        roleRepo.removeByName(roleName);
        return ResponseEntity.ok("Role deleted");
    }

}
