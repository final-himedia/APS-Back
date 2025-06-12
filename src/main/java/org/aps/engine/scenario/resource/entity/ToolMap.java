package org.aps.engine.scenario.resource.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tool_map")
public class ToolMap {
    @Id
    private String siteId;
    private String toolSize;
    private String scenarioId;
    private String partId;
    private String toolId;
    private String partName;

}
