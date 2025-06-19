package org.aps.engine.execution.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class ExecutionResponse {
        private String operationId;
        private String operationName;
        private String runTime;

        private String partSiteId;
        private String partId;
        private String partType;
        private String routingId;
        private String partName;

        private String scenarioId;
    }


