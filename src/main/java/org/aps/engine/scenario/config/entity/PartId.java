package org.aps.engine.scenario.config.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;


@Embeddable
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PartId implements Serializable {
    private String siteId;
    private String partId;
    private String partType;
}
