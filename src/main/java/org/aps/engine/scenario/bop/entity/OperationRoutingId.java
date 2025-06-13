package org.aps.engine.scenario.bop.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OperationRoutingId implements Serializable {
    private String scenarioId;
    private String siteId;
    private String routingId;
    private String operationId;
    private Integer operationSeq;
}