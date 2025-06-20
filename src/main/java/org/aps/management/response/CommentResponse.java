package org.aps.management.response;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponse {
    private Integer id;
    private String content;
    private LocalDateTime wroteAt;
    private String email;
    private String name;
}
