package org.aps.management.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "qnas")
public class Qna {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "writer_id", nullable = true)
    private Long writerId;
    private String title;
    private String content;
    private LocalDateTime wroteAt;
    private Boolean deleted = false;
    private String category;
}
