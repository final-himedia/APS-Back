package org.aps.engine.execution.service;

import lombok.RequiredArgsConstructor;
import org.aps.engine.execution.repository.WorkcenterPlanRepository;
import org.aps.engine.execution.response.ExecutionResponse;
import org.aps.engine.execution.result.WorkcenterPlan;
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
        System.out.println("===== 시뮬레이션 시작 =====");

        Map<ToolMaster, LocalDateTime> toolAvailability = initToolAvailability();
        Map<WorkCenter, LocalDateTime> wcAvailability = initWorkcenterAvailability();

        System.out.println("총 Tool 수: " + toolAvailability.size());
        System.out.println("총 WorkCenter 수: " + wcAvailability.size());

        List<ExecutionResponse> routes = operationRouteRepository.findByScenarioIdWithPart(scenarioId);
        List<WorkcenterPlan> resultPlans = new ArrayList<>();

        for (ExecutionResponse route : routes) {
            ToolMaster selectedTool = getEarliestAvailable(toolAvailability);
            if (selectedTool == null) continue;

            // WorkCenter 선택
            WorkCenterMap selectedWCMap = getEarliestWorkcenter(
                    workCenterMapRepository.findByRoutingId(route.getRoutingId()),
                    wcAvailability
            );
            if (selectedWCMap == null) continue;

            WorkCenter selectedWC = selectedWCMap.getWorkCenter();

            LocalDateTime startTime = Collections.max(List.of(
                    toolAvailability.get(selectedTool),
                    wcAvailability.get(selectedWC)));

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
                    .operationId(route.getOperationId())
                    .operationName(route.getOperationName())
                    .operationType("N/A") // 필요시 route에서 가져오도록 추가
                    .build();

            resultPlans.add(plan);
        }

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

    private ToolMaster getEarliestAvailable(Map<ToolMaster, LocalDateTime> availabilityMap) {
        return availabilityMap.entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    private WorkCenterMap getEarliestWorkcenter(List<WorkCenterMap> wcMaps, Map<WorkCenter, LocalDateTime> wcAvailability) {
        return wcMaps.stream()
                .min(Comparator.comparing(map -> wcAvailability.get(map.getWorkCenter())))
                .orElse(null);
    }
}
