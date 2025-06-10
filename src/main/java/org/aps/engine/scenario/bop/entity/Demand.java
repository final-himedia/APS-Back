package org.aps.engine.scenario.bop.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
