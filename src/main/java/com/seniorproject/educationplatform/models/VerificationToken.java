package com.seniorproject.educationplatform.models;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
public class VerificationToken implements Serializable {
    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_VERIFIED = "VERIFIED";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "VER_SEQ")
    @SequenceGenerator(name="VER_SEQ", sequenceName = "VER_SEQ", allocationSize = 1)
    private Long id;
    private String token;
    private String status;
    private LocalDateTime expiredDateTime;
    private LocalDateTime issuedDateTime;
    private LocalDateTime confirmedDateTime;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    public VerificationToken(){
        this.token = UUID.randomUUID().toString();
        this.issuedDateTime = LocalDateTime.now();
        this.expiredDateTime = this.issuedDateTime.plusDays(1);
        this.status = STATUS_PENDING;
    }

}
