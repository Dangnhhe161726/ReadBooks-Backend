package com.example.backend.models.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileDTO {
    private String fullName;
    private String email;
    private String phoneNumber;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dob; // Sử dụng LocalDate để chuyển đổi định dạng ngày sinh

    private String address;
    private boolean gender;

    // Phương thức để chuyển đổi LocalDate sang định dạng chuỗi yyyy-MM-dd
    public String getFormattedDob() {
        return this.dob.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
