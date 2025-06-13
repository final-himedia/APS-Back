package org.aps.engine.scenario.resource.entity;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class WorkCenterId implements Serializable {
    private String siteId;
    private String workcenterId;
    private String scenarioId;

}

