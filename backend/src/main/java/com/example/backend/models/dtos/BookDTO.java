package com.example.backend.models.dtos;

import com.example.backend.models.entities.Author;
import com.example.backend.models.entities.Category;
import com.example.backend.models.entities.Feedback;
import com.example.backend.models.entities.Notificate;
import com.example.backend.models.entities.UserEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {
  private Long id;
  private String name;
  private int view;
  private int favorites;
  @JsonProperty("create_time")
  private Date createTime;
  @JsonProperty("update_time")
  private Date updateTime;
  private String introduce;
  private boolean status;
  @JsonProperty("author_id")
  private long authorId;
  private List<Long> categories;
}
