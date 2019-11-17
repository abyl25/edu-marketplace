package com.seniorproject.educationplatform.services;

import com.seniorproject.educationplatform.models.User;
import com.seniorproject.educationplatform.models.VerificationToken;
import com.seniorproject.educationplatform.repositories.UserRepo;
import com.seniorproject.educationplatform.repositories.VerificationTokenRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class VerificationTokenService {
    private UserRepo userRepository;
    private VerificationTokenRepo verificationTokenRepo;
    private EmailSenderService emailSenderService;

    @Autowired
    public VerificationTokenService(UserRepo userRepository, VerificationTokenRepo verificationTokenRepo, EmailSenderService emailSenderService) {
        this.userRepository = userRepository;
        this.verificationTokenRepo = verificationTokenRepo;
        this.emailSenderService = emailSenderService;
    }

    public void createVerification(User user) {
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setUser(user);
        verificationTokenRepo.save(verificationToken);

        String to = user.getEmail();
        String from = "EduMarketplace Team <abylay.tastanbekov@nu.edu.kz>";
        String subject = "Confirm account!";
        String text = "Hi " + user.getFirstName() + " " + user.getLastName() + ",\n\n" +
                "To confirm your account, please click here: https://edu-mp.herokuapp.com/confirm?token=" + verificationToken.getToken() + "\n\n" +
                "Kind regards,\n" + "Education platform team,\n" + "Astana, Kazakhstan";
        emailSenderService.sendEmail(to, from, subject, text);
    }

    public ResponseEntity verifyAccount(String token) {
        VerificationToken verificationToken = verificationTokenRepo.findByToken(token);
        if (verificationToken == null) {
            return ResponseEntity.badRequest().body("Broken token");
        }
        if (verificationToken.getExpiredDateTime().isBefore(LocalDateTime.now())) {
            return ResponseEntity.unprocessableEntity().body("Expired token");
        }

        verificationToken.setConfirmedDateTime(LocalDateTime.now());
        verificationToken.setStatus(VerificationToken.STATUS_VERIFIED);
        User user = verificationToken.getUser();
        user.setEnabled(true);
        userRepository.save(user);
        verificationTokenRepo.save(verificationToken);

        return ResponseEntity.ok("You have successfully verified your account!");
    }
}
