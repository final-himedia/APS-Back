package org.aps.engine.execution.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.aps.engine.scenario.bop.entity.Operation;
import org.aps.engine.scenario.resource.entity.ToolMaster;
import org.aps.engine.scenario.resource.entity.WorkCenter;

import java.time.LocalDateTime;
    @Setter
    @Getter
    @Builder
    public class OperationSchedule {
        private Operation operation;
        private ToolMaster tool;
        private WorkCenter workcenter;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
    }

