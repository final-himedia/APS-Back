package org.aps.engine.scenario.resource.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;
import org.aps.engine.execution.entity.Operation;
import org.aps.engine.execution.entity.Workcenter;

@Entity
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
