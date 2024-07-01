package com.example.backend.models.dtos;

import com.example.backend.models.entities.Author;
import com.example.backend.models.entities.Category;
import com.example.backend.models.entities.Feedback;
import com.example.backend.models.entities.Notificate;
import com.example.backend.models.entities.UserEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class BookDTO {
  private Long id;

  private String name;

  private String link;

  private int view;

  private int favorites;

  private String thumbnail;

  private Date createTime;

  private Date updateTime;

  private String introduce;

  private boolean status;


  private Author author;


  private UserEntity userEntity;


  private List<Feedback> feedbacks;


  private List<Notificate> notificates;

  private List<Category> categories = new ArrayList<>();
}
