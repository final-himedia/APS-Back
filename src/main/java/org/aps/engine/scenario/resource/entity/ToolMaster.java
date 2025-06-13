package org.aps.engine.scenario.resource.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tool_master")
public class ToolMaster {

    @EmbeddedId
    private ToolMasterId toolMasterId;
    private String toolState;
    private Integer toolCavity;
    private String toolName;
}
