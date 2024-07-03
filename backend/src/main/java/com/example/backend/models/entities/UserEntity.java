package com.example.backend.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "phone_number", length = 10, nullable = false)
    private String phoneNumber;

    @Column(name = "date_of_brith")
    @Temporal(TemporalType.DATE)
    private Date dob;

    @Column(nullable = false)
    private String password;

    private String avatar;
    private String address;
    private boolean gender;

    @Column(name = "facebook_account_id")
    private String facebookAccountId;

    @Column(name = "google_account_id")
    private String googleAccountId;

    @Column(name = "is_active")
    private boolean active;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private List<Role> roles = new ArrayList<>();

    @OneToMany(mappedBy = "userEntity",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<Feedback> feedbacks = new ArrayList<>();

    @OneToMany(mappedBy = "userEntity",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<UserBook> userBooks = new ArrayList<>();

    @OneToMany(mappedBy = "userEntity",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<Notificate> notificates = new ArrayList<>();

    @OneToMany(mappedBy = "userEntity",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<Token> tokens = new ArrayList<>();
}
