package org.aps.engine.scenario.bop.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "part")
public class Part {

    @EmbeddedId
    private PartId partId;
    private String scenarioId;
    private String routingId;
    private String partName;
    private Integer minBatchSize;
    private Integer maxBatchSize;
    private String uom;

}
