package org.aps.common.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;

@Setter
@Getter
public class LoginRequest {
    @Email @NotBlank
    private String email;

    @NotBlank
    private String password;
}
