package org.aps.engine.result.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BomId implements Serializable {
    private  String fromSiteId;
    private  String fromPartId;
    private  String toSiteId;
    private  String toPartId;
    private  String zseq;

}
