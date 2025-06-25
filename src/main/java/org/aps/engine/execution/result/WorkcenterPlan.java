package org.aps.engine.execution.result;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "workcenter_plan")
public class WorkcenterPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer no;
    private String workcenterId;
    private String workcenterGroup;
    private String workcenterName;
    private String scenarioId;
    private LocalDateTime workcenterStartTime;
    private LocalDateTime workcenterEndTime;
    private String operationId;
    private String operationName;
    private String operationType;
    private String toolId;
    private String toolName;
    private String toPartId;
    private String fromPartId;
    private Double inQty;
    private String inUom;
    private Double outQty;
    private String outUom;
    private Integer toolSize;
}
