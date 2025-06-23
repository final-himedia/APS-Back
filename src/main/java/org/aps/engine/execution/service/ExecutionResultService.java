package org.aps.engine.execution.service;

import lombok.RequiredArgsConstructor;
import org.aps.engine.execution.repository.WorkcenterPlanRepository;
import org.aps.engine.execution.result.WorkcenterPlan;
import org.aps.engine.scenario.bop.entity.OperationRoute;
import org.aps.engine.scenario.bop.repository.OperationRouteRepository;
import org.aps.engine.scenario.resource.entity.ToolMaster;
import org.aps.engine.scenario.resource.entity.WorkCenter;
import org.aps.engine.scenario.resource.entity.WorkCenterMap;
import org.aps.engine.scenario.resource.repository.ToolMasterRepository;
import org.aps.engine.scenario.resource.repository.WorkCenterMapRepository;
import org.aps.engine.scenario.resource.repository.WorkCenterRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ExecutionResultService {

    private final WorkCenterRepository workcenterRepository;
    private final WorkcenterPlanRepository workcenterPlanRepository;
    private final OperationRouteRepository operationRouteRepository;
    private final WorkCenterMapRepository workCenterMapRepository;
    private final ToolMasterRepository toolMasterRepository;

    private static final LocalDateTime START_TIME = LocalDateTime.of(2025, 6, 4, 9, 0);

    public List<WorkcenterPlan> simulateSchedule(String scenarioId) {
        Map<ToolMaster, LocalDateTime> toolAvailability = initToolAvailability();
        Map<WorkCenter, LocalDateTime> wcAvailability = initWorkcenterAvailability();

        List<OperationRoute> routes = operationRouteRepository.findById_ScenarioId(scenarioId);
        System.out.println("선택된 routes: " + routes.size());
        List<WorkcenterPlan> resultPlans = new ArrayList<>();

        for (OperationRoute route : routes) {
            ToolMaster selectedTool = null;
            for (ToolMaster candidate : toolAvailability.keySet()) {
                if (selectedTool == null ||
                        toolAvailability.get(candidate).isBefore(toolAvailability.get(selectedTool))) {
                    selectedTool = candidate;
                }
            }
            if (selectedTool == null) continue;

            List<WorkCenterMap> wcMaps = workCenterMapRepository.findByRoutingId(route.getRoutingId());
            WorkCenterMap selectedWCMap = null;
            for (WorkCenterMap wcMap : wcMaps) {
                if (selectedWCMap == null ||
                        wcAvailability.get(wcMap.getWorkCenter()).isBefore(wcAvailability.get(selectedWCMap.getWorkCenter()))) {
                    selectedWCMap = wcMap;
                }
            }
            if (selectedWCMap == null) continue;

            WorkCenter selectedWC = selectedWCMap.getWorkCenter();

            LocalDateTime startTime = toolAvailability.get(selectedTool).isAfter(wcAvailability.get(selectedWC)) ?
                    toolAvailability.get(selectedTool) : wcAvailability.get(selectedWC);

            double tact = Double.parseDouble(selectedWCMap.getTactTime());
            LocalDateTime endTime = startTime.plusHours(Math.round(tact));


            toolAvailability.put(selectedTool, endTime);
            wcAvailability.put(selectedWC, endTime);

            WorkcenterPlan plan = WorkcenterPlan.builder()
                    .scenarioId(scenarioId)
                    .workcenterId(selectedWC.getWorkCenterId().getWorkcenterId())
                    .workcenterGroup(selectedWC.getWorkcenterGroup())
                    .workcenterName(selectedWC.getWorkcenterName())
                    .workcenterStartTime(startTime)
                    .workcenterEndTime(endTime)
                    .operationId(route.getId().getOperationId())
                    .operationName(route.getOperationName())
                    .operationType("")
                    .toolId(selectedTool.getToolMasterId().getToolId())
                    .toolName(selectedTool.getToolName())
                    .build();

            resultPlans.add(plan);
        }

        System.out.println("생성된 계획 수: " + resultPlans.size());

        workcenterPlanRepository.saveAll(resultPlans);
        return resultPlans;
    }

    private Map<ToolMaster, LocalDateTime> initToolAvailability() {
        Map<ToolMaster, LocalDateTime> map = new HashMap<>();
        for (ToolMaster tool : toolMasterRepository.findAll()) {
            map.put(tool, START_TIME);
        }
        return map;
    }

    private Map<WorkCenter, LocalDateTime> initWorkcenterAvailability() {
        Map<WorkCenter, LocalDateTime> map = new HashMap<>();
        for (WorkCenter wc : workcenterRepository.findAll()) {
            map.put(wc, START_TIME);
        }
        return map;
    }
}
