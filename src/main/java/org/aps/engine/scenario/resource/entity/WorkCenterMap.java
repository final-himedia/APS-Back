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
@Table(name = "workcenter_map")
public class WorkCenterMap {

    @EmbeddedId
    private WorkCenterMapId workCenterMapId;
    private String routingId;
    private String siteId;
    private String partId;
    private String routingGroup;
    private String routingVersion;
    private String tactTime;
    private String tactTimeUom;
    private String procTime;
    private String procTimeUom;
}
