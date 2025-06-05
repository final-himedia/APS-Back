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
    private String materialGroup;
    private String materialGroupDescription;
    private String abcCode;
    private String safetyStockQty;
    private String status;
    private String productFamilyValue;
    private String procurementType;
    private String specialProcurementType;
    private String labor;
    private LocalDateTime createDatetime;
    private String createBy;
    private LocalDateTime updateDatetime;
    private String updateBy;
    private String interfaceDate;
    private String interfaceTime;
    private String interfaceType;
    private String interfaceText;
    private String operationId;
    private LocalDateTime updateDate;
    private LocalDateTime createDate;
}
