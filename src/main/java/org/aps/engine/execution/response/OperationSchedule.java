package org.aps.engine.execution.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.aps.engine.execution.entity.Operation;
import org.aps.engine.execution.entity.Tool;
import org.aps.engine.execution.entity.Workcenter;
import java.time.LocalDateTime;

@Setter
@Getter
@Builder
public class OperationSchedule {
    private Operation operation;
    private Tool tool;
    private Workcenter workcenter;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
