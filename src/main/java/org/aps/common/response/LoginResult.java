package org.aps.common.response;

import lombok.*;
import org.aps.common.entity.User;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResult {
    private String token;
    private User user;
}
