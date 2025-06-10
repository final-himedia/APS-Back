package org.aps.engine.scenario.resource.entity;

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
@Table(name = "workcenter")
public class WorkCenter {

    @Id
    private String workcenterId;
    private String siteId;
    private String workcenterName;
    private String workcenterGroup;
    private String workcenterType;
    private String priorityId;
    private String dispatcherType;
    private String workcenterState;
    private String automation;
    private String scenarioId;
}
