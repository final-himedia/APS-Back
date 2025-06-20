package org.aps.engine.scenario.bop.entity;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
public class OperationRouteId implements Serializable {
    private String siteId;

    private String operationId;
    private String scenarioId;

}
