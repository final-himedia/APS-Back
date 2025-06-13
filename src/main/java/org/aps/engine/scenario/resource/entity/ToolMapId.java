package org.aps.engine.scenario.resource.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ToolMapId implements Serializable {
    private String partId;
    private String scenarioId;
    private String toolId;

}
