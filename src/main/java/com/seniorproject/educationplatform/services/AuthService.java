package com.seniorproject.educationplatform.services;

import com.seniorproject.educationplatform.dto.auth.LoginRequestDto;
import com.seniorproject.educationplatform.dto.auth.PasswordUpdateDto;
import com.seniorproject.educationplatform.dto.auth.SignUpRequestDto;
import com.seniorproject.educationplatform.exception.CustomException;
import com.seniorproject.educationplatform.exception.UsernameAlreadyExistsException;
import com.seniorproject.educationplatform.models.Cart;
import com.seniorproject.educationplatform.models.PasswordResetToken;
import com.seniorproject.educationplatform.models.Role;
import com.seniorproject.educationplatform.models.User;
import com.seniorproject.educationplatform.repositories.PasswordTokenRepo;
import com.seniorproject.educationplatform.repositories.RoleRepo;
import com.seniorproject.educationplatform.security.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@Transactional
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RoleRepo roleRepo;
    private final UserService userService;
    private final PasswordTokenRepo passwordTokenRepo;
    private final VerificationTokenService verificationTokenService;
    private final EmailSenderService emailSenderService;

    @Autowired
    public AuthService(AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider,
                   UserService userService, RoleRepo roleRepo, VerificationTokenService verificationTokenService,
                   PasswordTokenRepo passwordTokenRepo, EmailSenderService emailSenderService) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.roleRepo = roleRepo;
        this.userService = userService;
        this.passwordTokenRepo= passwordTokenRepo;
        this.verificationTokenService = verificationTokenService;
        this.emailSenderService = emailSenderService;
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

    public void signUp(SignUpRequestDto signUpRequestDto) {
        User user = userService.signUpDtoToEntity(signUpRequestDto);  // modelMapper.map(signUpRequestDto, User.class);
        boolean userExists = userService.existsByUserName(user.getUserName());
        Long type = (long) signUpRequestDto.getUserType();
        Role role = roleRepo.findById(type).orElse(null);
        List<Role> roles = new ArrayList<>();
        roles.add(role);

        if (!userExists) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(roles);

            Cart cart = new Cart();
            user.setCart(cart);
            cart.setStudent(user);

            userService.save(user);
            verificationTokenService.createVerification(user);
        } else {
            log.info("AuthService signup: Username is already in use");
            throw new UsernameAlreadyExistsException("Username is already in use");
        }
    }

    public ResponseEntity createPasswordResetToken(String email) {
        User user = userService.findByEmail(email);
        if (user == null) {
            return ResponseEntity.badRequest().body("User with email" + email + " does not exist");
        }
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setUser(user);
        passwordTokenRepo.save(passwordResetToken);

        String to = user.getEmail();
        String from = "EduMarketplace Team <abylay.tastanbekov@nu.edu.kz>";
        String subject = "Password Reset";
        String text = "Hi " + user.getFirstName() + " " + user.getLastName() + ",\n\n" +
                "To reset your password click here: http://localhost:8081/api/auth/resetPassword?id=" + user.getId() + "&token=" + passwordResetToken.getToken() + "\n\n" +
                "Kind regards,\n" + "Education platform team,\n" + "Astana, Kazakhstan";
        emailSenderService.sendEmail(to, from, subject, text);
        return ResponseEntity.ok("We have sent you an email with password reset link");
    }

    public ResponseEntity resetPassword(long id, String token) {
        PasswordResetToken passwordResetToken = passwordTokenRepo.findByToken(token);
        if ((passwordResetToken == null) || (passwordResetToken.getUser().getId() != id)) {
            return ResponseEntity.badRequest().body("Broken token");
        }
        if (passwordResetToken.getExpiryDateTime().isBefore(LocalDateTime.now())) {
            return ResponseEntity.unprocessableEntity().body("Expired token");
        }

        passwordResetToken.setResetDateTime(LocalDateTime.now());
        User user = passwordResetToken.getUser();
        Authentication auth = new UsernamePasswordAuthenticationToken(user, null, Arrays.asList(new SimpleGrantedAuthority("CHANGE_PASSWORD_PRIVILEGE")));

        List<Role> roles = user.getRoles();
        roles.add(new Role("CHANGE_PASSWORD_PRIVILEGE"));
        userService.save(user);

        SecurityContextHolder.getContext().setAuthentication(auth);
        String tempToken = jwtTokenProvider.createToken(user.getUserName(), roles);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Valid password reset token");
        response.put("token", tempToken);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('CHANGE_PASSWORD_PRIVILEGE')")
    public ResponseEntity updatePassword(PasswordUpdateDto passwordUpdateDto) {
        log.info("LOG: AuthService updatePassword");
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        for (GrantedAuthority grantedAuthority: userDetails.getAuthorities()) {
            System.out.println("upd ps, grantedAuthority: " + grantedAuthority.getAuthority());
        }

        User user = userService.findByUserName(userDetails.getUsername());
        System.out.println("LOG: AuthService, updatePassword, user: " + user);
        user.setPassword(passwordEncoder.encode(passwordUpdateDto.getPassword()));

        Role role = roleRepo.findByName("CHANGE_PASSWORD_PRIVILEGE");
        user.getRoles().remove(role);

        userService.save(user);
        return ResponseEntity.ok("Password updated successfully! Log in again");
    }

    public UserDetails getLoggedInUser() {
        Authentication auth = getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            return (UserDetails) auth.getPrincipal();
        }
        return null;
    }

    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public User me(HttpServletRequest req) {
        return userService.findByUserName(jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(req)));
    }

}
