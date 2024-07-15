package com.example.backend.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificattionDTO {
    private Long bookId;
    private String createTime;
    private String description;
    private String linkBook;
    private String title;
}
