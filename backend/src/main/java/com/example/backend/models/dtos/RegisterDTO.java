package com.example.backend.models.dtos;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
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
public class RegisterDTO {
    @JsonProperty("full_name")
    @NotBlank(message = "Full name is not blank")
    private String fullName;

    @Email(message = "Email is not valid")
    private String email;

    @JsonProperty("phone_number")
    @Pattern(regexp = "[0-9]{10}", message = "Phone number it not valid")
    private String phoneNumber;

    @NotBlank(message = "New password is not blank")
    private String password;

    @NotBlank(message = "Repassword is not blank")
    private String repassword;

    @NotBlank(message = "Address is not blank")
    private String address;

    private boolean gender;

    private Date dob;

    @NotEmpty(message = "Role is not empty")
    private List<Long> roles;
}
