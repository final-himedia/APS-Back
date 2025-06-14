package org.aps.engine.scenario.bop.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;


@Entity(name = "BopPart")
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "part")
public class Part {

    @EmbeddedId
    private PartId partId;
    private String routingId;
    private String partName;
    private Integer minBatchSize;
    private Integer maxBatchSize;
    private String uom;

}
