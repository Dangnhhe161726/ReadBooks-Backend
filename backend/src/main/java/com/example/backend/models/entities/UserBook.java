package com.example.backend.models.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "user_book")
public class UserBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

}
