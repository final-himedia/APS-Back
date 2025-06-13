package org.aps.engine.scenario.resource.entity;

import jakarta.persistence.*;
import lombok.*;

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
    private String operationId;
    private String routingGroup;
    private String routingVersion;
    @Embedded
    private WorkCenterId workcenterId;
    private String tactTime;
    @Column(name = "tack_time_uom")
    private String tactTimeUom;
    private String procTime;
    private String procTimeUom;

}
