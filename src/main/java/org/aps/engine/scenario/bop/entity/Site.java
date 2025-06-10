package org.aps.engine.scenario.bop.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "site")
public class Site {

    @Id
    private String siteId;
    private String scenarioId;
    private String siteName;
}
