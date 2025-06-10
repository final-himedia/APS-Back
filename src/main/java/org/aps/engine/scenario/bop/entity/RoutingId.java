package org.aps.engine.scenario.bop.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoutingId implements Serializable {
    private String siteId;
    private String routingId;
}
