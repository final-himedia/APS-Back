package org.aps.engine.execution.service;

import lombok.RequiredArgsConstructor;
import org.aps.engine.execution.response.OperationSchedule;
import org.aps.engine.scenario.bop.entity.Operation;
import org.aps.engine.scenario.bop.entity.OperationRouting;
import org.aps.engine.scenario.bop.repository.OperationRoutingRepository;
import org.aps.engine.scenario.resource.entity.ToolMaster;
import org.aps.engine.scenario.resource.entity.WorkCenter;
import org.aps.engine.scenario.resource.repository.ToolMasterRepository;
import org.aps.engine.scenario.resource.repository.WorkCenterMapRepository;
import org.aps.engine.scenario.resource.repository.WorkCenterRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ExecutionService {
    private final OperationRoutingRepository operationRoutingRepository;
    private final WorkCenterMapRepository workcenterMapRepository;
    private final ToolMasterRepository toolRepository;
    private final WorkCenterRepository workcenterRepository;

    public List<OperationSchedule> simulateSequentialSchedule() {
        List<OperationSchedule> schedules = new ArrayList<>();
        List<OperationRouting> operationRoutings = operationRoutingRepository.findAll(Sort.by("sequence").ascending());

        // 툴들이 언제 투입이 가능한지를 관리할 용도로 맵을 만들고
        Map<ToolMaster, LocalDateTime> toolAvailableTime = new HashMap<>();
        toolRepository.findAll().forEach(tool -> {
            toolAvailableTime.put(tool, LocalDateTime.of(2025, 6, 4, 9, 0));
        });
        // 작업장에 언제 투입이 가능한지를 관리할 용도로 맵을 만들고,
        Map<WorkCenter, LocalDateTime> workcenterAvailableTime = new HashMap<>();
        workcenterRepository.findAll().forEach(workcenter -> {
            workcenterAvailableTime.put(workcenter, LocalDateTime.of(2025, 6, 4, 9, 0));
        });


        for (OperationRouting operationRouting : operationRoutings) {
            Operation task = operationRouting.getOperationRoutingId().getOperationId().getOperation();

            Tool selectedTool = null;
            for(Tool candidate :toolAvailableTime.keySet()) {
                if(selectedTool == null || toolAvailableTime.get(candidate).isBefore(toolAvailableTime.get(selectedTool))) {
                    selectedTool = candidate;
                }
            }
            Workcenter selectedWorkcenter = null;
            List<WorkcenterMap> workcenterMaps = workcenterMapRepository.findByOperation(task);
            for(WorkcenterMap workcenterMap : workcenterMaps) {
                if(selectedWorkcenter == null ||
                        workcenterAvailableTime.get(workcenterMap.getWorkcenter()).isBefore(workcenterAvailableTime.get(selectedWorkcenter)) ) {
                    selectedWorkcenter = workcenterMap.getWorkcenter();
                }
            }

            LocalDateTime startTime = toolAvailableTime.get(selectedTool).isAfter(workcenterAvailableTime.get(selectedWorkcenter)) ?
                    toolAvailableTime.get(selectedTool) : workcenterAvailableTime.get(selectedWorkcenter)  ;

            LocalDateTime endTime = startTime.plusMinutes(task.getDuration());

            toolAvailableTime.put(selectedTool, endTime);
            workcenterAvailableTime.put(selectedWorkcenter, endTime);


            OperationSchedule schedule = OperationSchedule.builder()
                    .operation(task).tool(selectedTool).workcenter(selectedWorkcenter).startTime(startTime).endTime(endTime).build();
            schedules.add(schedule);
        }
        return null;
   }
}
