package org.aps.engine.scenario.target.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Entity(name = "BopDemand")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "demand")
public class Demand {
    @EmbeddedId
    private DemandId demandId;

    private String scenarioId;
    private String partName;
    private String customerId;
    private LocalDateTime dueDate;
    private Double demandQty;
    private Float priority;
    private String uom;
    private String orderType;
    private String orderTypeName;
    private String exceptYn;
    private LocalDateTime headerCreationDate;
    private Boolean hasOverActQty;
}
