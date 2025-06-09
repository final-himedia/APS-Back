package org.aps.engine.scenario.config.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PriorityId {
    private String scenarioId;
    private String priorityId;
    private String factorId;
}
