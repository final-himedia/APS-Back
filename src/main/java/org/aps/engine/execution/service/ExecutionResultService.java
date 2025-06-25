package org.aps.engine.execution.service;

import lombok.RequiredArgsConstructor;
import org.aps.engine.execution.repository.WorkcenterPlanRepository;
import org.aps.engine.execution.result.WorkcenterPlan;
import org.aps.engine.scenario.bop.entity.Bom;
import org.aps.engine.scenario.bop.entity.OperationRoute;
import org.aps.engine.scenario.bop.repository.BomRepository;
import org.aps.engine.scenario.bop.repository.OperationRouteRepository;
import org.aps.engine.scenario.resource.entity.ToolMaster;
import org.aps.engine.scenario.resource.entity.WorkCenter;
import org.aps.engine.scenario.resource.entity.WorkCenterMap;
import org.aps.engine.scenario.resource.repository.ToolMasterRepository;
import org.aps.engine.scenario.resource.repository.WorkCenterMapRepository;
import org.aps.engine.scenario.resource.repository.WorkCenterRepository;
import org.springframework.cglib.core.Local;
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
    private final BomRepository bomRepository;

    public List<WorkcenterPlan> simulateSchedule(String scenarioId) {
        LocalDateTime engineStart = LocalDateTime.now();
        LocalDateTime startTime = LocalDateTime.of(2025, 6, 24, 0, 0);

        List<WorkcenterPlan> resultPlans = new ArrayList<>();

        Map<ToolMaster, LocalDateTime> toolAvailableTime = new HashMap<>();
        toolMasterRepository.findAll().forEach(tool -> toolAvailableTime.put(tool, startTime));

        Map<WorkCenter, LocalDateTime> workcenterAvailableTime = new HashMap<>();
        workcenterRepository.findAll().forEach(wc -> workcenterAvailableTime.put(wc, startTime));

        List<OperationRoute> operationRoutes = operationRouteRepository.findById_ScenarioIdOrderByOperationSeq(scenarioId);
        List<Bom> boms = bomRepository.findByBomId_ScenarioId(scenarioId);
        Map<String, List<Bom>> bomByOperationId = boms.stream().collect(Collectors.groupingBy(Bom::getOperationId));

        Map<String, Integer> producedParts = new HashMap<>();
        for (Bom bom : boms) producedParts.put(bom.getToPartName(), 0);

        Map<String, Integer> availablePartsQty = new HashMap<>();
        for (Bom bom : boms) {
            String fromPart = bom.getFromPartName();
            if (!producedParts.containsKey(fromPart)) {
                availablePartsQty.put(fromPart, Integer.MAX_VALUE);
            }
        }

        for (OperationRoute route : operationRoutes) {
            String opId = route.getId().getOperationId();
            List<Bom> routeBoms = bomByOperationId.get(opId);
            if (routeBoms == null || routeBoms.isEmpty()) continue;

            boolean canExecute = true;
            for (Bom bom : routeBoms) {
                int available = availablePartsQty.getOrDefault(bom.getFromPartName(), 0);
                int needed = (int) Double.parseDouble(bom.getInQty());
                if (available < needed) {
                    canExecute = false;
                    break;
                }
            }
            if (!canExecute) continue;

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

            for (Bom bom : routeBoms) {
                int inQty = (int) Double.parseDouble(bom.getInQty());
                int outQty = (int) Double.parseDouble(bom.getOutQty());

                double procTimeHour;
                try {
                    procTimeHour = Double.parseDouble(selectedWcMap.getProcTime());
                } catch (Exception e) {
                    procTimeHour = 1.0;
                }
                long procTimeMinutes = (long) Math.ceil(procTimeHour * 60 * inQty);

                LocalDateTime endTime = selectedStart.plusMinutes(procTimeMinutes);

                // 자원 사용 시간 갱신
                workcenterAvailableTime.put(selectedWorkcenter, endTime);
                toolAvailableTime.put(selectedTool, endTime);

                // 자재 수량 갱신
                int fromQty = availablePartsQty.getOrDefault(bom.getFromPartName(), 0);
                availablePartsQty.put(bom.getFromPartName(), fromQty - inQty);

                int toQty = availablePartsQty.getOrDefault(bom.getToPartName(), 0);
                availablePartsQty.put(bom.getToPartName(), toQty + outQty);

                WorkcenterPlan plan = WorkcenterPlan.builder()
                        .scenarioId(scenarioId)
                        .operationId(route.getId().getOperationId())
                        .operationName(route.getOperationName())
                        .operationType(route.getOperationType())
                        .workcenterId(selectedWorkcenter.getWorkCenterId().getWorkcenterId())
                        .workcenterName(selectedWorkcenter.getWorkcenterName())
                        .workcenterGroup(selectedWorkcenter.getWorkcenterGroup())
                        .workcenterStartTime(selectedStart)
                        .workcenterEndTime(endTime)
                        .toolId(selectedTool.getToolMasterId().getToolId())
                        .toolName(selectedTool.getToolName())
                        .fromPartId(bom.getFromPartName())
                        .toPartId(bom.getToPartName())
                        .inQty((double) inQty)
                        .inUom(bom.getInUom())
                        .outQty((double) outQty)
                        .outUom(bom.getOutUom())
                        .build();

                resultPlans.add(plan);
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

        Set<String> toolIds = new HashSet<>();
        for (WorkcenterPlan plan : plans) {
            toolIds.add(plan.getToolId());
        }

        List<ToolMaster> result = new ArrayList<>();
        for (ToolMaster tool : allTools) {
            String currentToolId = tool.getToolMasterId().getToolId();
            if (toolIds.contains(currentToolId)) {
                result.add(tool);
            }
        }

        return result;
    }
}

