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
    private RoutingId RoutingId;
    private String Description;
    private String ScenarioId;
    private String RoutingName;
    private String CreateDatetime;
    private String CreateBy;
    private String UpdateDatetime;
    private String UpdateBy;
    private String RoutingType;

}
