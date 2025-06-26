package org.aps.analysis.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Anal {
    @Id
    private Long id;
    private String version;
    private String status;
    private Long durationMinutes;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String errorMessage;
    private String userId;
    private String scenarioId;
}
