package org.aps.engine.scenario.bop.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.aps.engine.scenario.bop.entity.OperationRouteId;

import java.math.BigDecimal;

@Entity
@Table(name = "operation_route")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OperationRoute {

    @EmbeddedId
    private OperationRouteId id;
    private String routingId;

    private String operationName;

    private Integer runTime;
    private String runTimeUom;

    private Integer waitTime;
    private String waitTimeUom;

    private Integer transferTime;
    private String transferTimeUom;

    private Integer operationSeq;
    private String operationType;

    private String sourcingType;

    private Double  yield;
}
