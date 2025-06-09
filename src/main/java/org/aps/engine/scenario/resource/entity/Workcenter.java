package org.aps.engine.scenario.resource.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
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
