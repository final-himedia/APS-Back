package org.aps.engine.scenario.entity;

import jakarta.persistence.*;
import lombok.*;
import java.sql.Timestamp;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "scenario")
public class Scenario {

    @Id
    private String scenarioId;
    private String name;
    private String description;
    private Timestamp createdAt;
}
