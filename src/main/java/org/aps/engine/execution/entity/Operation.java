package org.aps.engine.execution.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity(name = "ExecutionOperation")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Operation {
    @Id
    private String id;
    private String name;
    private Integer duration;
}
