package org.aps.engine.execution.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Entity(name = "ExecutionWorkcenterMap")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkcenterMap {
    @Id
    private Long id;

    @ManyToOne
    private Operation operation;

    @ManyToOne
    private Workcenter workcenter;

}
