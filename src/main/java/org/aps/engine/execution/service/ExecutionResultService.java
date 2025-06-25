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
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExecutionResultService {

    private final WorkCenterRepository workcenterRepository;
    private final WorkcenterPlanRepository workcenterPlanRepository;
    private final OperationRouteRepository operationRouteRepository;
    private final WorkCenterMapRepository workCenterMapRepository;
    private final ToolMasterRepository toolMasterRepository;

    public List<WorkcenterPlan> simulateSchedule(String scenarioId) {
        LocalDateTime startTime = LocalDateTime.of(2025, 6, 24, 0, 0);
        List<WorkcenterPlan> resultPlans = new ArrayList<>();

        Map<ToolMaster, LocalDateTime> toolAvailableTime = new HashMap<>();
        toolMasterRepository.findAll().forEach(tool -> toolAvailableTime.put(tool, startTime));

        Map<WorkCenter, LocalDateTime> workcenterAvailableTime = new HashMap<>();
        workcenterRepository.findAll().forEach(wc -> workcenterAvailableTime.put(wc, startTime));

        List<OperationRoute> operationRoutes = operationRouteRepository.findById_ScenarioIdOrderByOperationSeq(scenarioId);
        Map<String, List<OperationRoute>> routesByRouting = operationRoutes.stream()
                .collect(Collectors.groupingBy(OperationRoute::getRoutingId));

        for (String routingId : routesByRouting.keySet()) {
            List<OperationRoute> routedOps = routesByRouting.get(routingId);

            for (OperationRoute route : routedOps) {
                List<WorkCenterMap> wcMaps = workCenterMapRepository.findByRoutingId(route.getRoutingId());
                if (wcMaps.isEmpty()) continue;

                ToolMaster selectedTool = null;
                WorkCenter selectedWorkcenter = null;
                WorkCenterMap selectedWcMap = null;
                LocalDateTime selectedStart = null;

                for (WorkCenterMap wcMap : wcMaps) {
                    WorkCenter wc = wcMap.getWorkCenter();
                    LocalDateTime wcReady = workcenterAvailableTime.getOrDefault(wc, startTime);

                    for (ToolMaster tool : toolAvailableTime.keySet()) {
                        LocalDateTime toolReady = toolAvailableTime.getOrDefault(tool, startTime);
                        LocalDateTime candidateStart = wcReady.isAfter(toolReady) ? wcReady : toolReady;

                        if (selectedStart == null || candidateStart.isBefore(selectedStart)) {
                            selectedStart = candidateStart;
                            selectedTool = tool;
                            selectedWorkcenter = wc;
                            selectedWcMap = wcMap;
                        }
                    }
                }

                if (selectedTool == null || selectedWorkcenter == null || selectedWcMap == null) continue;

                double procTimeHour;
                try {
                    procTimeHour = Double.parseDouble(selectedWcMap.getProcTime());
                } catch (Exception e) {
                    procTimeHour = 1.0;
                }

                double unitProcTime = procTimeHour * 60; // in minutes
                LocalDateTime unitEnd = selectedStart.plusMinutes((long) unitProcTime);

                WorkcenterPlan plan = WorkcenterPlan.builder()
                        .scenarioId(scenarioId)
                        .routingId(route.getRoutingId())
                        .operationId(route.getId().getOperationId())
                        .operationName(route.getOperationName())
                        .operationType(route.getOperationType())
                        .workcenterId(selectedWorkcenter.getWorkCenterId().getWorkcenterId())
                        .workcenterName(selectedWorkcenter.getWorkcenterName())
                        .workcenterGroup(selectedWorkcenter.getWorkcenterGroup())
                        .workcenterStartTime(selectedStart)
                        .workcenterEndTime(unitEnd)
                        .toolId(selectedTool.getToolMasterId().getToolId())
                        .toolName(selectedTool.getToolName())
                        .build();

                resultPlans.add(plan);

                // 자원 가용 시간 업데이트
                workcenterAvailableTime.put(selectedWorkcenter, unitEnd);
                toolAvailableTime.put(selectedTool, unitEnd);
            }
        }

        workcenterPlanRepository.saveAll(resultPlans);
        return resultPlans;
    }

    public List<OperationRoute> getRoutesByScenario(String scenarioId) {
        return operationRouteRepository.findById_ScenarioIdOrderByOperationSeq(scenarioId);
    }

    public List<WorkCenter> getWorkcenterByScenario(String scenarioId) {
        return workcenterRepository.findByWorkCenterIdScenarioId(scenarioId);
    }

    public List<ToolMaster> getUsedToolsByScenario(String scenarioId) {
        List<WorkcenterPlan> plans = simulateSchedule(scenarioId);
        List<ToolMaster> allTools = toolMasterRepository.findAll();

        Set<String> toolIds = plans.stream()
                .map(WorkcenterPlan::getToolId)
                .collect(Collectors.toSet());

        return allTools.stream()
                .filter(tool -> toolIds.contains(tool.getToolMasterId().getToolId()))
                .collect(Collectors.toList());
    }
}
