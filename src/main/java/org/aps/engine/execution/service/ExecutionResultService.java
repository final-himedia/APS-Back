package org.aps.engine.execution.service;

import lombok.RequiredArgsConstructor;
import org.aps.engine.execution.repository.WorkcenterPlanRepository;
import org.aps.engine.execution.result.WorkcenterPlan;
import org.aps.engine.scenario.bop.entity.Operation;
import org.aps.engine.scenario.bop.entity.OperationRouting;
import org.aps.engine.scenario.bop.repository.OperationRepository;
import org.aps.engine.scenario.bop.repository.OperationRoutingRepository;
import org.aps.engine.scenario.resource.entity.*;
import org.aps.engine.scenario.resource.repository.ToolMapRepository;
import org.aps.engine.scenario.resource.repository.ToolMasterRepository;
import org.aps.engine.scenario.resource.repository.WorkCenterMapRepository;
import org.aps.engine.scenario.resource.repository.WorkCenterRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ExecutionResultService {

    private final OperationRepository operationRepository;
    private final ToolMasterRepository toolRepository;
    private final WorkCenterRepository workcenterRepository;
    private final WorkcenterPlanRepository workcenterPlanRepository;
    private final OperationRoutingRepository bopOperationRoutingRepository;
    private final ToolMapRepository resourceToolMapRepository;
    private final WorkCenterMapRepository resourceWorkCenterMapRepository;
    private final ToolMasterRepository resourceToolMasterRepository;

    public List<WorkcenterPlan> simulateSchedule(String scenarioId) {
        List<OperationRouting> operationRoutings = bopOperationRoutingRepository.findAll();

        Map<ToolMaster, LocalDateTime> toolAvailableTime = new HashMap<>();
        resourceToolMasterRepository.findAll().forEach(tool -> {
            toolAvailableTime.put(tool, LocalDateTime.of(2025, 6, 4, 9, 0));
        });

        Map<WorkCenter, LocalDateTime> workcenterAvailableTime = new HashMap<>();
        workcenterRepository.findAll().forEach(workCenter -> {
            workcenterAvailableTime.put(workCenter, LocalDateTime.of(2025, 6, 4, 9, 0));
        });

        List<WorkcenterPlan> resultPlans = new ArrayList<>();

        for (OperationRouting operationRouting : operationRoutings) {
            Operation task = operationRouting.getOperation();

            // 최적의 Tool 선택
            ToolMaster selectedTool = toolAvailableTime.entrySet().stream()
                    .min(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse(null);

            if (selectedTool == null) continue;

            // 해당 Operation에 가능한 Workcenter 목록 중 가용 시간이 가장 빠른 것 선택
            List<WorkCenterMap> workCenterMaps = resourceWorkCenterMapRepository.findByOperation(task);
            WorkCenter selectedWorkcenter = null;
            WorkCenterMap selectedWorkcenterMap = null;

            for (WorkCenterMap map : workCenterMaps) {
                WorkCenter wc = map.getWorkCenter();
                if (selectedWorkcenter == null || workcenterAvailableTime.get(wc).isBefore(workcenterAvailableTime.get(selectedWorkcenter))) {
                    selectedWorkcenter = wc;
                    selectedWorkcenterMap = map;
                }
            }

            if (selectedWorkcenter == null || selectedWorkcenterMap == null) continue;

            LocalDateTime toolAvailable = toolAvailableTime.get(selectedTool);
            LocalDateTime workcenterAvailable = workcenterAvailableTime.get(selectedWorkcenter);
            LocalDateTime startTime = toolAvailable.isAfter(workcenterAvailable) ? toolAvailable : workcenterAvailable;

            // ⬇️ tactTime을 시간 단위로 해석하여 endTime 계산
            double tactTime = Double.parseDouble(selectedWorkcenterMap.getTactTime());
            long tactHours = Math.round(tactTime);
            LocalDateTime endTime = startTime.plusHours(tactHours);

            // 자원 사용 시간 업데이트
            toolAvailableTime.put(selectedTool, endTime);
            workcenterAvailableTime.put(selectedWorkcenter, endTime);

            // 계획 저장
            WorkcenterPlan plan = WorkcenterPlan.builder()
                    .workcenterId(selectedWorkcenter.getWorkCenterId().getWorkcenterId())
                    .workcenterGroup(selectedWorkcenter.getWorkcenterGroup())
                    .workcenterName(selectedWorkcenter.getWorkcenterName())
                    .scenarioId(scenarioId)
                    .workcenterStartTime(startTime)
                    .workcenterEndTime(endTime)
                    .operationId(task.getOperationId().getOperationId())
                    .operationName(task.getOperationName())
                    .operationType(task.getOperationType())
                    .build();

            resultPlans.add(plan);
        }

        workcenterPlanRepository.saveAll(resultPlans);

        return resultPlans;
    }
}
