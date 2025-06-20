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
public class WorkCenterId implements Serializable {
    private String siteId;
    private String workcenterId;
    private String scenarioId;
}
