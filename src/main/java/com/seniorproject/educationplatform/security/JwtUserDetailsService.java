package com.seniorproject.educationplatform.security;

import com.seniorproject.educationplatform.models.User;
import com.seniorproject.educationplatform.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class JwtUserDetailsService implements UserDetailsService {
    private UserService userService;

    @Autowired
    public JwtUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userService.findByUserName(userName);

        if (user == null) {
            throw new UsernameNotFoundException("Username " + userName + " not found");
        }

        JwtUser jwtUser = JwtUserFactory.create(user);
        log.info("LOG: IN loadUserByUsername - user with username: {} successfully loaded", userName);
        return jwtUser;
    }
}
