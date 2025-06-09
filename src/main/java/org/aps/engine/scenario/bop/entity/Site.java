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
    private String SiteId;
    private String ScenarioId;
    private String SiteName;
    private String CreateDatetime;
    private String CreateBy;
    private String UpdateDatetime;
    private String UpdateBy;
    private String InterfaceDate;
    private String InterfaceTime;
    private String InterfaceType;
    private String InterfaceText;

}
