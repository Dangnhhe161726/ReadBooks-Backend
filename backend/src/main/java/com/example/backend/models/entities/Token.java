package com.example.backend.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tokens")
@NoArgsConstructor
@AllArgsConstructor
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 300)
    private String token;

    @Column(name = "refresh_token", length = 300)
    private String refreshToken;

    @Column(name = "token_type",nullable = false, length = 50)
    private String tokenType;

    @Column(name = "expiration_date")
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private LocalDateTime expirationDate;

    @Column(name = "refesh_expiration_date")
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private LocalDateTime refeshExpirationDate;

    @Column(name = "is_mobile", columnDefinition = "TINYINT(1)")
    private boolean isMobile;

    private boolean revoked;
    private boolean expired;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;
}
