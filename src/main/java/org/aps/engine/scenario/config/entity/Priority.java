package org.aps.engine.scenario.config.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "priority")
public class Priority {

    @EmbeddedId
    private PriorityId priorityId;
    private String factorDetail;
    private String factorType;
    private Float factorWeight;
    private String orderType;
    private Integer sequence;
    private String criteria;
    private String description;


}
