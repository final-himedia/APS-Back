package org.aps.engine.scenario.bop.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;
import org.aps.engine.execution.entity.Operation;
import org.aps.engine.execution.entity.Routing;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OperationRouting {
    @Id
    private Long id;
    @ManyToOne
    private Routing routing;
    @ManyToOne
    private Operation operation;

    private Integer sequence;
}
