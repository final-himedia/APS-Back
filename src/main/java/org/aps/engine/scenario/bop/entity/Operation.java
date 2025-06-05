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
    private String ScenarioId;
    private String OperationName;
    private String RunTime;
    private String RunTimeUom;
    private String Yield;
    private String WaitTime;
    private String WaitTimeUom;
    private String TransferTime;
    private String TransferTimeUom;
    private String OperationSeq;
    private String OperationType;
    private String SourcingType;
    private String CreateDatetime;
    private String CreateBy;
    private String UpdateDatetime;
    private String UpdateBy;
}
