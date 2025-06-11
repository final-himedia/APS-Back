package org.aps.common.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FindPasswordRequest {
    @NotBlank
    @Email
    private String email;

}
