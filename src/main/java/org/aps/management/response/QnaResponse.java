package org.aps.management.response;

import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QnaResponse {
    private Integer id;
    private Long writerId;
    private String title;
    private String content;
    private LocalDateTime wroteAt;
    private String email;
    private String name;
    private String category;
}
