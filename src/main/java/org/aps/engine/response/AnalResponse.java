package org.aps.engine.response;

import lombok.*;
import org.aps.engine.execution.result.WorkcenterPlan;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalResponse {
    private List<WorkcenterPlan> plans;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
