package org.aps.engine.scenario.bop.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "routing")
public class Routing {

    @EmbeddedId
    private RoutingId routingId;
    private String description;
    private String scenarioId;
    private String routingName;
    private String createDatetime;
    private String createBy;
    private String updateDatetime;
    private String updateBy;
    private String routingType;

}
