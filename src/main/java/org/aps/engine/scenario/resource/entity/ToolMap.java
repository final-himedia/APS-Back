package org.aps.engine.scenario.resource.entity;

import jakarta.persistence.*;
import lombok.*;
import org.aps.engine.scenario.bop.entity.OperationRoute;

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

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "site_id", referencedColumnName = "siteId", insertable = false, updatable = false),
            @JoinColumn(name = "operation_id", referencedColumnName = "operationId", insertable = false, updatable = false),
            @JoinColumn(name = "scenario_id", referencedColumnName = "scenarioId", insertable = false, updatable = false)
    })
    private OperationRoute operationRoute;


    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "tool_id", referencedColumnName = "toolId"),
            @JoinColumn(name = "site_id", referencedColumnName = "siteId")
    })
    private ToolMaster toolMaster;

}
