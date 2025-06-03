package org.jun.project.entity;

import jakarta.persistence.Embeddable;

import lombok.*;

@Embeddable
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BomId {
    private  String fromSiteId;
    private  String fromPartId;
    private  String toSiteId;
    private  String toPartId;
    private  String zseq;

}
