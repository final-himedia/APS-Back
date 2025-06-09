package org.aps.engine.scenario.bop.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoutingId {
    private String siteId;
    private String routingId;
}
