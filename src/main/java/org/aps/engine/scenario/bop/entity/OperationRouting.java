package org.aps.engine.scenario.bop.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "operation_routing")
public class OperationRouting {

    @EmbeddedId
    private OperationRoutingId operationRoutingId;

    private String scenarioId;
    private String operationName;
    private String operationType;
    private LocalDateTime createDatetime;
    private String createBy;
    private LocalDateTime updateDatetime;
    private String updateBy;
}
