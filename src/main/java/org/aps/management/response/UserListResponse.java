package org.aps.management.response;

import lombok.*;


@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserListResponse {
    private Long id;
    private String email;
    private String name;
    private String role;
}

