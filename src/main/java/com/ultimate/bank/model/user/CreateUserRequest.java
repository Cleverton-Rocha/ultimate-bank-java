package com.ultimate.bank.model.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;

public record CreateUserRequest(
        @NotNull
        @Size(min = 11, max = 11, message = "CPF must have 11 characters.")
        @Pattern(regexp = "^[0-9]\\d*$", message = "CPF must have only numbers, without special characters or letters.")
        String CPF,
        @NotNull(message = "First name cannot be null")
        @Size(min = 1, message = "First name must have at least 1 character.")
        String firstName,

        @NotNull(message = "Last name cannot be null")
        @Size(min = 1, message = "Last name must have at least 1 character.")
        String lastName,

        @NotNull @Email(message = "Write a valid email.")
        String email,
        @NotNull(message = "Password cannot be null")
        @Size(min = 8, max = 100, message = "Password must have at least 8 characters.")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$",
                 message = "Password must have at least one uppercase letter, one lowercase letter and one number.")
        String password) {
}
