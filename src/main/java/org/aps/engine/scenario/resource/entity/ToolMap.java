package org.aps.engine.scenario.resource.entity;

import jakarta.persistence.*;
import lombok.*;
import org.aps.engine.scenario.bop.entity.Operation;

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
    private String toolSize;
    private String partName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "operation_id", referencedColumnName = "operationId"),
            @JoinColumn(name = "scenario_id", referencedColumnName = "scenarioId")
    })
    private Operation operation;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "tool_id", referencedColumnName = "toolId"),
            @JoinColumn(name = "site_id", referencedColumnName = "siteId")
    })
    private ToolMaster toolMaster;

}
