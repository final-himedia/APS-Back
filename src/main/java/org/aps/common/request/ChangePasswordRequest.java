package org.aps.common.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequest {
    @NotBlank @Email
    private String email;
    @NotBlank
    private String oldPassword;
    @NotBlank
    private String newPassword;
}
