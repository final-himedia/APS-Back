package org.aps.engine.scenario.resource.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class WorkCenterId implements Serializable {
    private String siteId;
    private String workcenterId;
    private String scenarioId;

}

