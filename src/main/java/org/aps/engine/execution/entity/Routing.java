package org.aps.engine.execution.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity(name = "ExecutionRouting")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Routing {
    @Id
    private String id;
    private String name;
    private String description;
}
