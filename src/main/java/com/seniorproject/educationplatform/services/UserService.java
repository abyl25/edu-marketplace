package com.seniorproject.educationplatform.services;

import com.seniorproject.educationplatform.dto.SignUpRequestDto;
import com.seniorproject.educationplatform.models.User;
import com.seniorproject.educationplatform.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private UserRepo userRepo;

    @Autowired
    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public User save(User user) {
        return userRepo.save(user);
    }

    public List<User> getUsers() {
        return userRepo.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepo.findById(id);
    }

    public User findByUserName(String userName) {
        return userRepo.findByUserName(userName);
    }

    public boolean existsByUserName(String userName) {
        return userRepo.existsByUserName(userName);
    }

    public void delete(User user) {
        userRepo.delete(user);
    }

    // Model Mapper
    public User signUpDtoToEntity(SignUpRequestDto requestDto) {
        User user = new User();
        user.setFirstName(requestDto.getFirstName());
        user.setLastName(requestDto.getLastName());
        user.setUserName(requestDto.getUserName());
        user.setEmail(requestDto.getEmail());
        user.setPassword(requestDto.getPassword());
        return user;
    }

//    public User loginDtoToEntity() {
//
//    }

}
