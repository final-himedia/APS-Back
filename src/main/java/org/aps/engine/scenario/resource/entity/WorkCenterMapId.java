package org.aps.engine.scenario.resource.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkCenterMapId implements Serializable {
    private String operationId;
    private String scenarioId;
    private String workcenterId;
}
