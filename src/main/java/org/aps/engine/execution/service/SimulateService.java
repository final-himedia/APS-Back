package org.aps.engine.execution.service;


import lombok.RequiredArgsConstructor;
import org.aps.engine.execution.entity.*;
import org.aps.engine.execution.repository.OperationRoutingRepository;
import org.aps.engine.execution.repository.ToolRepository;
import org.aps.engine.execution.repository.WorkcenterMapRepository;
import org.aps.engine.execution.repository.WorkcenterRepository;
import org.aps.engine.execution.response.OperationSchedule;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SimulateService {
    private final OperationRoutingRepository operationRoutingRepository;
    private final WorkcenterMapRepository workcenterMapRepository;
    private final ToolRepository toolRepository;
    private final WorkcenterRepository workcenterRepository;

    public List<OperationSchedule> simulateSequentialSchedule() {
        List<OperationSchedule> schedules = new ArrayList<>();
        List<OperationRouting> operationRoutings = operationRoutingRepository.findAll(Sort.by("sequence").ascending());

        Map<Tool, LocalDateTime> toolAvailableTime = new HashMap<>();
        toolRepository.findAll().forEach(tool -> {
            toolAvailableTime.put(tool, LocalDateTime.of(2025, 6, 4, 9, 0));
        });


        Map<Workcenter, LocalDateTime> workcenterAvailableTime = new HashMap<>();
        workcenterRepository.findAll().forEach(workcenter -> {
            workcenterAvailableTime.put(workcenter, LocalDateTime.of(2025, 6, 4, 9, 0));
        });


        for (OperationRouting operationRouting : operationRoutings) {
            Operation task = operationRouting.getOperation();

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
        return schedules;
    }
}




