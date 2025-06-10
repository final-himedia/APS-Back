package org.aps.engine.execution.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity(name = "ExecutionDemand")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Demand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long demandId;

    @Column(name = "SITE_ID")
    private String siteId;

    @ManyToOne
    @JoinColumn(name = "PART_ID")
    private Part part;

    @Column(name = "PART_NAME")
    private String partName;

    @Column(name = "CUSTOMER_ID")
    private String customerId;

    @Column(name = "DUE_DATE")
    private LocalDate dueDate;

    @Column(name = "DEMAND_QTY")
    private Integer demandQty;

    @Column(name = "PRIORITY")
    private Integer priority;

    @Column(name = "UOM")
    private String uom;

    @Column(name = "ORDER_TYPE")
    private String orderType;

    @Column(name = "ORDER_TYPE_NAME")
    private String orderTypeName;

    @Column(name = "EXCEPT_YN")
    private String exceptYn;

    @Column(name = "HEADER_CREATION_DATE")
    private LocalDateTime headerCreationDate;

    @Column(name = "HAS_OVER_ACT_QTY")
    private Boolean hasOverActQty;

    @Column(name = "SCENARIO_ID")
    private String scenarioId;
}
