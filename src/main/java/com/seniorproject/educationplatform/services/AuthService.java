package com.seniorproject.educationplatform.services;

import com.seniorproject.educationplatform.dto.LoginRequestDto;
import com.seniorproject.educationplatform.dto.SignUpRequestDto;
import com.seniorproject.educationplatform.exception.CustomException;
import com.seniorproject.educationplatform.models.Role;
import com.seniorproject.educationplatform.models.User;
import com.seniorproject.educationplatform.repositories.RoleRepo;
import com.seniorproject.educationplatform.security.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final RoleRepo roleRepo;

    @Autowired
    public AuthService(AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider,
                   UserService userService, RoleRepo roleRepo) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.roleRepo = roleRepo;
    }

    public String login(LoginRequestDto loginRequestDto) {
        System.out.println("loginReqDto: " + loginRequestDto);
        String token;
        String userName = loginRequestDto.getUserName();
        String password = loginRequestDto.getPassword();
        int userType = loginRequestDto.getUserType();
        User user = userService.findByUserName(userName);
        if (user == null) {
            throw new UsernameNotFoundException("User with username: " + userName + " not found");
        }
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, password));
        token = jwtTokenProvider.createToken(userName, user.getRoles());
        return token;
    }

    public String signUp(SignUpRequestDto signUpRequestDto) {
        User user = userService.signUpDtoToEntity(signUpRequestDto);  // modelMapper.map(signUpRequestDto, User.class);
        boolean userExists = userService.existsByUserName(user.getUserName());
        Long type = (long) signUpRequestDto.getUserType();
        Role role = roleRepo.findById(type).orElse(null);
//        System.out.println("role: " + role);
        List<Role> roles = new ArrayList<>();
        roles.add(role);

        String token;
        if (!userExists) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(roles);
            userService.save(user);
            token = jwtTokenProvider.createToken(user.getUserName(), user.getRoles());
        } else {
            log.info("AuthService signup: Username is already in use");
            throw new CustomException("Username is already in use", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return token;
    }

    public User me(HttpServletRequest req) {
        return userService.findByUserName(jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(req)));
    }
    

}
