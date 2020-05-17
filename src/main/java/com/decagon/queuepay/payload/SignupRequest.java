package com.decagon.queuepay.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SignupRequest {

    @NotBlank(message = "Please name should not be empty")
    private String fullName;

    @NotBlank(message = "Please phone number should not be empty")
    private String phoneNumber;

    @Email(message = "must not be blank")
    @NotBlank(message = "Please email should not be empty")
    private String username;

    @NotBlank(message = "Please password should not be empty")
    @Size(min = 6, max = 16, message = "Please password should be between 6 to 16 characters long")
    private String password;
}
