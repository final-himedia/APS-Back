package org.aps.engine.scenario.bop.entity;

import jakarta.persistence.*;
import lombok.*;
import org.aps.engine.scenario.bop.entity.OperationRoutingId;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OperationRouting {

    @EmbeddedId
    private OperationRoutingId operationRoutingId;

    private String operationName;
    private String operationType;
    private LocalDateTime createDatetime;
    private String createBy;
    private LocalDateTime updateDatetime;
    private String updateBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "operation_id", referencedColumnName = "operationId"),
            @JoinColumn(name = "scenario_id", referencedColumnName = "scenarioId")
    })
    private Operation operation;
}
