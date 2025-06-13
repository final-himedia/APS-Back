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

    @EmbeddedId
    private ToolMapId toolMapId;
    private String siteId;
    private String toolSize;
    private String partName;

}
