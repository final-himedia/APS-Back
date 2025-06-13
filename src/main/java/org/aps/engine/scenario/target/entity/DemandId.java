package org.aps.engine.scenario.target.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DemandId implements Serializable {
    private String demandId;
    private String siteId;
    private String partId;
    private String scenarioId;

}