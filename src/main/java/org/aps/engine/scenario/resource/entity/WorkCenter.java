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
@Table(name = "workcenter")
public class WorkCenter {

    @EmbeddedId
    private WorkCenterId workCenterId;
    private String workcenterName;
    private String workcenterGroup;
    private String workcenterType;
    private String priorityId;
    private String dispatcherType;
    private String workcenterState;
    private String automation;
}
