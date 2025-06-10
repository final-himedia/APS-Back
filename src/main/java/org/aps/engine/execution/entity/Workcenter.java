package org.aps.engine.execution.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity(name = "ExecutionWorkcenter")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Workcenter {
    @Id
    private String id;
    private String name;
}
