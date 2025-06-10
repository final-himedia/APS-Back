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

    private String factorType;
    private String orderType;
    private Integer sequence;
    private String description;

}
