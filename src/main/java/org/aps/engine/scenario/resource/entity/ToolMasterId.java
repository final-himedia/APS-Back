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
public class ToolMasterId implements Serializable {
    private String toolId;
    private String siteId;
    private String scenarioId;
}
