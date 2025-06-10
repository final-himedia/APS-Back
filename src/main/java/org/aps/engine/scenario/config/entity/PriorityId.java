package org.aps.engine.scenario.config.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PriorityId implements Serializable {
    private String scenarioId;
    private String priorityId;
    private String factorId;
}
