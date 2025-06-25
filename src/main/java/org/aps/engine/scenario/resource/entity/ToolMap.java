package org.aps.engine.scenario.resource.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tool_map")
@Getter
@Setter
public class ToolMap {

    @Id
    private String partId;

    private String partName;
    private String toolSize;

    @Column(name = "site_id")
    private String siteId;

    @Column(name = "operation_id")
    private String operationId;

    @Column(name = "scenario_id")
    private String scenarioId;

    @Column(name = "tool_id")
    private String toolId;

    @Column(name = "workcenter_id")
    private String workcenterId;
}
