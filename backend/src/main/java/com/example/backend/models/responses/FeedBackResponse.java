package com.example.backend.models.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedBackResponse {
  private Long id;
  private String content;
  private int numLike;
  private UserResponse userEntity;
}
