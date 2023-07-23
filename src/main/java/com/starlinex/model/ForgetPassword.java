package com.starlinex.model;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForgetPassword {

    @Email(regexp = "^\\S+@\\S+\\.\\S+$", message = "Invalid email.")
    private String email;
}
