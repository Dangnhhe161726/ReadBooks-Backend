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
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = true, length = 450)
    private String link;

    private int view;

    private int favorites;
    @Column(nullable = true, length = 450)
    private String thumbnail;

    @Column(name = "create_time", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date createTime;

    @Column(name = "update_time")
    @Temporal(TemporalType.DATE)
    private Date updateTime;

   @Column(nullable = true, length = 10000)
    private String introduce;

    @Column(nullable = false)
    private boolean status;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    @OneToMany(mappedBy = "book",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<BookMark> bookMarks = new ArrayList<>();

    @OneToMany(mappedBy = "book",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<UserBook> userBooks = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "category_book",
            joinColumns = @JoinColumn(name = "book_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "category_id", referencedColumnName = "id"))
    private List<Category> categories = new ArrayList<>();

}