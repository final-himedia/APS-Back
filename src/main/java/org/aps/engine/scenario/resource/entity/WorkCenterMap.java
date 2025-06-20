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
@Table(name = "workcenter_map")
public class WorkCenterMap {

    @Id
    private String routingId;
    private String partId;
    private String routingGroup;
    private String routingVersion;
    private String tactTime;
    private String tactTimeUom;
    private String procTime;
    private String procTimeUom;
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "scenario_id", referencedColumnName = "scenarioId", insertable = false, updatable = false),
            @JoinColumn(name = "site_id", referencedColumnName = "siteId", insertable = false, updatable = false),
            @JoinColumn(name = "workcenter_id", referencedColumnName = "workcenterId", insertable = false, updatable = false)
    })
    private WorkCenter workCenter;
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "site_id", referencedColumnName = "siteId", insertable = false, updatable = false),
            @JoinColumn(name = "operation_id", referencedColumnName = "operationId", insertable = false, updatable = false),
            @JoinColumn(name = "scenario_id", referencedColumnName = "scenarioId", insertable = false, updatable = false)
    })
    private OperationRoute operationRoute;



}
