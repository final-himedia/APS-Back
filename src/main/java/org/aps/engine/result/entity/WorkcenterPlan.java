package org.aps.engine.result.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "workcenter_plan")
public class WorkcenterPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer no;
    private String scenarioId;
    private String workcenterId;
    private String workcenterName;
    private String operationId;
    private String operationName;
    private String operationType;
    private LocalDateTime workcenterStartTime;
    private LocalDateTime workcenterEndTime;
    private String workcenterGroup;
    private String toolId;
    private String toolName;
    private String routingId;
}

