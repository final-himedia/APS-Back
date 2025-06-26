package org.aps.engine.execution.service;

import jakarta.transaction.Transactional;
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

import java.time.Duration;
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

    @Transactional
    public List<WorkcenterPlan> simulateSchedule(String scenarioId) {
        LocalDateTime startTime = LocalDateTime.of(2025, 6, 24, 0, 0);
        List<WorkcenterPlan> resultPlans = new ArrayList<>();

        // 1. Tool 초기화 (scenarioId 기준)
        Map<String, LocalDateTime> toolAvailableTime = new HashMap<>();
        Map<String, ToolMaster> toolMap = new HashMap<>();
        List<ToolMaster> tools = toolMasterRepository.findAllByToolMasterId_ScenarioId(scenarioId);
        for (ToolMaster tool : tools) {
            String toolId = tool.getToolMasterId().getToolId();
            toolAvailableTime.put(toolId, startTime);
            toolMap.put(toolId, tool);
        }


        // 2. WorkCenter 초기화
        Map<String, LocalDateTime> workcenterAvailableTime = new HashMap<>();
        Map<String, WorkCenter> workcenterMap = new HashMap<>();
        for (WorkCenter wc : workcenterRepository.findAllByWorkCenterId_ScenarioId(scenarioId)) {
            String wcId = wc.getWorkCenterId().getWorkcenterId();
            workcenterAvailableTime.put(wcId, startTime);
            workcenterMap.put(wcId, wc);
        }

        // 3. OperationRoute를 operationSeq 순서대로 전부 가져오기 (라우팅 무시)
        List<OperationRoute> operationRoutes = operationRouteRepository.findById_ScenarioIdOrderByOperationSeq(scenarioId);

        // 라우팅별 마지막 작업 완료 시간 기록 (필요하면 유지, 없으면 무시)
        Map<String, LocalDateTime> lastEndTimeByRouting = new HashMap<>();

        for (OperationRoute route : operationRoutes) {
            String routingId = route.getId().getRoutingId();

            // 이 operation에 해당하는 WorkCenterMap들 (라우팅, 시나리오 조건)
            List<WorkCenterMap> wcMaps = workCenterMapRepository.findByOperationRoute_Id_RoutingIdAndOperationRoute_Id_ScenarioId(routingId, scenarioId);

            String selectedWorkcenterId = null;
            WorkCenterMap selectedWcMap = null;
            LocalDateTime selectedStart = null;

            // 가장 빠른 워크센터 사용 가능 시간 찾기
            for (WorkCenterMap wcMap : wcMaps) {
                String wcId = wcMap.getWorkCenter().getWorkCenterId().getWorkcenterId();
                LocalDateTime wcReady = workcenterAvailableTime.getOrDefault(wcId, startTime);
                LocalDateTime prevOpEnd = lastEndTimeByRouting.getOrDefault(routingId, startTime);

                // 이전 작업 종료 및 워크센터 가용시간 고려
                LocalDateTime candidateStart = Collections.max(Arrays.asList(wcReady, prevOpEnd));

                if (selectedStart == null || candidateStart.isBefore(selectedStart)) {
                    selectedStart = candidateStart;
                    selectedWorkcenterId = wcId;
                    selectedWcMap = wcMap;
                }
            }

            // 툴 선택: 가장 빨리 준비된 툴 선택
            String selectedToolId = null;
            LocalDateTime earliestToolReady = null;
            for (String toolId : toolAvailableTime.keySet()) {
                LocalDateTime toolReady = toolAvailableTime.get(toolId);
                if (earliestToolReady == null || toolReady.isBefore(earliestToolReady)) {
                    earliestToolReady = toolReady;
                    selectedToolId = toolId;
                }
            }

            LocalDateTime toolReady = toolAvailableTime.get(selectedToolId);

            // 실제 시작 시간 = 워크센터, 툴, 이전 작업 완료 시간 중 가장 늦은 시간
            LocalDateTime startAt = Collections.max(Arrays.asList(selectedStart, toolReady));

            double procTimeHour;
            try {
                procTimeHour = Double.parseDouble(selectedWcMap.getProcTime());
            } catch (Exception e) {
                procTimeHour = 1.0;
            }
            long procMinutes = (long) (procTimeHour * 60);
            LocalDateTime unitEnd = startAt.plusMinutes(procMinutes);

            ToolMaster selectedTool = toolMap.get(selectedToolId);
            WorkCenter selectedWorkcenter = workcenterMap.get(selectedWorkcenterId);

            WorkcenterPlan plan = WorkcenterPlan.builder()
                    .scenarioId(scenarioId)
                    .routingId(route.getId().getRoutingId())
                    .operationId(route.getId().getOperationId())
                    .operationName(route.getOperationName())
                    .operationType(route.getOperationType())
                    .workcenterId(selectedWorkcenterId)
                    .workcenterName(selectedWorkcenter.getWorkcenterName())
                    .workcenterGroup(selectedWorkcenter.getWorkcenterGroup())
                    .workcenterStartTime(startAt)
                    .workcenterEndTime(unitEnd)
                    .toolId(selectedToolId)
                    .toolName(selectedTool.getToolName())
                    .build();

            resultPlans.add(plan);

            // 리소스 사용 시간 업데이트
            workcenterAvailableTime.put(selectedWorkcenterId, unitEnd);
            toolAvailableTime.put(selectedToolId, unitEnd);
            lastEndTimeByRouting.put(routingId, unitEnd);
        }
        if (workcenterPlanRepository.findAllByScenarioId(scenarioId)!=null){
            workcenterPlanRepository.deleteAllByScenarioId(scenarioId);
            workcenterPlanRepository.saveAll(resultPlans);
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

    public List<Map<String, Object>> getExecutionResult(String scenarioId) {
        List<WorkcenterPlan> plans = workcenterPlanRepository.findAllByScenarioId(scenarioId);
        List<Map<String, Object>> result = new ArrayList<>();

        // 라우팅 ID로 그룹핑
        Map<String, List<WorkcenterPlan>> grouped = plans.stream()
                .collect(Collectors.groupingBy(WorkcenterPlan::getRoutingId));

        // 라우팅 그룹을 시작일 기준으로 정렬
        List<Map.Entry<String, List<WorkcenterPlan>>> sortedGroups = grouped.entrySet().stream()
                .sorted(Comparator.comparing(entry ->
                        entry.getValue().stream()
                                .map(WorkcenterPlan::getWorkcenterStartTime)
                                .min(LocalDateTime::compareTo)
                                .orElse(LocalDateTime.MAX)))
                .collect(Collectors.toList());

        int taskId = 1;

        for (Map.Entry<String, List<WorkcenterPlan>> entry : sortedGroups) {
            String routingId = entry.getKey();
            List<WorkcenterPlan> groupPlans = entry.getValue();

            // 그룹 내 작업들을 시작 시간 기준 정렬
            groupPlans.sort(Comparator.comparing(WorkcenterPlan::getWorkcenterStartTime));

            // 부모 Task 생성
            Map<String, Object> parentTask = new HashMap<>();
            parentTask.put("TaskID", taskId);
            parentTask.put("TaskName", routingId);

            LocalDateTime groupStart = groupPlans.stream()
                    .map(WorkcenterPlan::getWorkcenterStartTime)
                    .min(LocalDateTime::compareTo)
                    .orElse(null);

            LocalDateTime groupEnd = groupPlans.stream()
                    .map(WorkcenterPlan::getWorkcenterEndTime)
                    .max(LocalDateTime::compareTo)
                    .orElse(null);

            parentTask.put("StartDate", groupStart);
            parentTask.put("EndDate", groupEnd);

            result.add(parentTask);

            int parentTaskId = taskId;
            taskId++;

            // 자식 Task 생성
            for (WorkcenterPlan plan : groupPlans) {
                Map<String, Object> childTask = new HashMap<>();
                childTask.put("TaskID", taskId);
                childTask.put("ParentId", parentTaskId);
                childTask.put("TaskName", plan.getOperationName());
                childTask.put("StartDate", plan.getWorkcenterStartTime());

                long duration = Duration.between(
                        plan.getWorkcenterStartTime(),
                        plan.getWorkcenterEndTime()
                ).toHours();
                childTask.put("Duration", duration);

                childTask.put("Workcenter", plan.getWorkcenterName());
                childTask.put("Tool", plan.getToolName());  // 툴 대신 워커 지정

                result.add(childTask);
                taskId++;
            }
        }
        return result;
    }


}
