package org.aps.engine.scenario.bop.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OperationRouteId implements Serializable {
    private String siteId;
    private String operationId;
    private String scenarioId;
}
