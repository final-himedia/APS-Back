package org.aps.engine.execution.result;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExecutionResult {
    private String scenarioId;
    private String versionNo;
    private String workcenterId;
    private String workcenterName;
    private String lotId;
    private String operationId;
    private String operationName;
    private String workcenterStateCode;
    private String operationType;
    private String siteId;
    private String partId;
    private String partName;
    private String routingId;
    private Integer processQty;
    private String uom;
    private LocalDateTime operationInTime;
    private Integer workcenterStartTime;
    private LocalDateTime workcenterEndTime;
    private LocalDateTime targetDate;
    private String demandId;
    private LocalDateTime dueDate;
    private String automation;
    private String workcenterPlanId;
    private String toolDetail1;
    private Integer inputProcTime;
    private Integer inputTactTime;
    private Double utilization;
    private Double efficiency;
    private Integer applyProcTime;
    private Integer applyTactTime;
    private String moPartName;
    private String exceptFlag;
    private String moPartId;
    private String staffNo;
    private String workcenterGroup;
}
