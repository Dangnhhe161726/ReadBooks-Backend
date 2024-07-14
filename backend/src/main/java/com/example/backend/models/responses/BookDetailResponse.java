package com.example.backend.models.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDetailResponse {
    private Long id;
    private String name;
    private String link;
    private int view;
    private int favorites;
    private String thumbnail;
    @JsonProperty("create_time")
    private Date createTime;
    @JsonProperty("update_time")
    private Date updateTime;
    private String introduce;
    private boolean status;
    private AuthorResponse author;
    private List<CategoryResponse> categories;
    private List<BookMarkResponse> bookMarks;
}
