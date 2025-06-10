package org.aps.engine.scenario.bop.entity;


import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "operation")
public class Operation {

    @EmbeddedId
    private OperationId operationId;

    private String scenarioId;
    private String operationName;
    private String runTime;
    private String runTimeUom;
    private String yield;
    private String waitTime;
    private String waitTimeUom;
    private String transferTime;
    private String transferTimeUom;
    private String operationSeq;
    private String operationType;
    private String sourcingType;
}
