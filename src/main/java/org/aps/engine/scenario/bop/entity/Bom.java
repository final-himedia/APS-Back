package org.aps.engine.scenario.bop.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity(name = "BopBom")
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "bom")
public class Bom {

    @EmbeddedId
    private BomId bomId;
    private  String bomCategory;
    private  String fromPartName;
    private  String operationId;
    private  String toPartName;
    private  String inQty;
    private  String inUom;
    private  String outQty;
    private  String outUom;
    private  String effStartDate;
    private  String createBy;
    private  String createDatetime;
    private  String bomVersion;
    private  String bomText;
    private  String fromPartLevel;
    private  String toPartLevel;
}
