package org.aps.engine.execution.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.aps.analysis.entity.Anal;
import org.aps.analysis.repository.AnalRepository;
import org.aps.analysis.request.AnalRequest;
import org.aps.engine.execution.repository.WorkcenterPlanRepository;


import org.aps.engine.execution.result.WorkcenterPlan;
import org.aps.engine.response.AnalResponse;
import org.aps.engine.scenario.bop.entity.OperationRoute;
import org.aps.engine.scenario.bop.repository.OperationRouteRepository;
import org.aps.engine.scenario.resource.entity.ToolMaster;
import org.aps.engine.scenario.resource.entity.WorkCenter;
import org.aps.engine.scenario.resource.entity.WorkCenterMap;
import org.aps.engine.scenario.resource.repository.ToolMasterRepository;
import org.aps.engine.scenario.resource.repository.WorkCenterMapRepository;
import org.aps.engine.scenario.resource.repository.WorkCenterRepository;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private final AnalRepository analRepository;

    @Transactional
    public AnalResponse simulateSchedule(String scenarioId) {
        LocalDateTime engineStart = LocalDateTime.now();
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

        // 3. OperationRoute를 operationSeq 순서대로 전부 가져오기 (bucketFlag 필터 제거)
        List<OperationRoute> allRoutes = operationRouteRepository.findById_ScenarioIdOrderByOperationSeq(scenarioId);

        // 라우팅별 작업 큐 생성
        Map<String, Queue<OperationRoute>> routingQueues = new HashMap<>();
        for (OperationRoute route : allRoutes) {
            routingQueues.computeIfAbsent(route.getId().getRoutingId(), k -> new LinkedList<>()).add(route);
        }

        // 라우팅별 마지막 작업 완료 시간 기록
        Map<String, LocalDateTime> lastEndTimeByRouting = new HashMap<>();

        // 4. 시뮬레이션 루프
        while (!routingQueues.isEmpty()) {
            List<ScheduledCandidate> candidates = new ArrayList<>();

            for (String routingId : routingQueues.keySet()) {
                Queue<OperationRoute> queue = routingQueues.get(routingId);
                if (queue == null || queue.isEmpty()) continue;

                OperationRoute nextOp = queue.peek();
                if (nextOp == null) continue;

                LocalDateTime prevEnd = lastEndTimeByRouting.getOrDefault(routingId, startTime);

                // 이 operation에 해당하는 WorkCenterMap들
                List<WorkCenterMap> wcMaps = workCenterMapRepository
                        .findByOperationRoute_Id_RoutingIdAndOperationRoute_Id_ScenarioId(routingId, scenarioId);

                if (wcMaps == null || wcMaps.isEmpty()) continue;

                for (WorkCenterMap wcMap : wcMaps) {
                    String wcId = wcMap.getWorkCenter().getWorkCenterId().getWorkcenterId();
                    LocalDateTime wcReady = workcenterAvailableTime.getOrDefault(wcId, startTime);

                    for (String toolId : toolAvailableTime.keySet()) {
                        LocalDateTime toolReady = toolAvailableTime.get(toolId);

                        LocalDateTime earliestStart = Collections.max(Arrays.asList(prevEnd, wcReady, toolReady));

                        double procTimeHour = parseProcTime(wcMap.getProcTime(), 1.0);
                        LocalDateTime endTime = earliestStart.plusHours((long) procTimeHour);

                        candidates.add(new ScheduledCandidate(nextOp, routingId, wcId, toolId, earliestStart, endTime, wcMap));
                    }
                }
            }

            if (candidates.isEmpty()) break; // 더 이상 스케줄링할 작업 없음

            // 가장 빨리 끝나는 작업 선택
            candidates.sort(Comparator.comparing(c -> c.endTime));
            ScheduledCandidate selected = candidates.get(0);

            ToolMaster selectedTool = toolMap.get(selected.toolId);
            WorkCenter selectedWc = workcenterMap.get(selected.wcId);

            WorkcenterPlan plan = WorkcenterPlan.builder()
                    .scenarioId(scenarioId)
                    .routingId(selected.operation.getId().getRoutingId())
                    .operationId(selected.operation.getId().getOperationId())
                    .operationName(selected.operation.getOperationName())
                    .operationType(selected.operation.getOperationType())
                    .workcenterId(selected.wcId)
                    .workcenterName(selectedWc.getWorkcenterName())
                    .workcenterGroup(selectedWc.getWorkcenterGroup())
                    .workcenterStartTime(selected.startTime)
                    .workcenterEndTime(selected.endTime)
                    .toolId(selected.toolId)
                    .toolName(selectedTool.getToolName())
                    .build();

            resultPlans.add(plan);

            // 자원 사용 시간 업데이트
            workcenterAvailableTime.put(selected.wcId, selected.endTime);
            toolAvailableTime.put(selected.toolId, selected.endTime);
            lastEndTimeByRouting.put(selected.routingId, selected.endTime);

            // 작업 큐에서 제거
            routingQueues.get(selected.routingId).poll();
            if (routingQueues.get(selected.routingId).isEmpty()) {
                routingQueues.remove(selected.routingId);
            }
        }

        // 기존 스케줄 삭제 후 저장
        if (workcenterPlanRepository.findAllByScenarioId(scenarioId) != null) {
            workcenterPlanRepository.deleteAllByScenarioId(scenarioId);
        }
        workcenterPlanRepository.saveAll(resultPlans);

        LocalDateTime finishEngine = LocalDateTime.now();

        return AnalResponse.builder()
                .plans(resultPlans)
                .endTime(finishEngine)
                .startTime(startTime)
                .build();
    }



    // 후보 작업 데이터 클래스
    private static class ScheduledCandidate {
        OperationRoute operation;
        String routingId;
        String wcId;
        String toolId;
        LocalDateTime startTime;
        LocalDateTime endTime;
        WorkCenterMap wcMap;

        public ScheduledCandidate(OperationRoute operation, String routingId, String wcId, String toolId,
                                  LocalDateTime startTime, LocalDateTime endTime, WorkCenterMap wcMap) {
            this.operation = operation;
            this.routingId = routingId;
            this.wcId = wcId;
            this.toolId = toolId;
            this.startTime = startTime;
            this.endTime = endTime;
            this.wcMap = wcMap;
        }
    }

    // helper 메서드
    private static double parseProcTime(String str, double defaultVal) {
        try {
            return Double.parseDouble(str);
        } catch (Exception e) {
            return defaultVal;
        }
    }

    public List<OperationRoute> getRoutesByScenario(String scenarioId) {
        return operationRouteRepository.findById_ScenarioIdOrderByOperationSeq(scenarioId);
    }

    public List<WorkCenter> getWorkcenterByScenario(String scenarioId) {
        return workcenterRepository.findByWorkCenterIdScenarioId(scenarioId);
    }

    public List<ToolMaster> getUsedToolsByScenario(String scenarioId) {
        List<WorkcenterPlan> plans = (List<WorkcenterPlan>) simulateSchedule(scenarioId);
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


    // 간트차트 데이터 생성 메소드
    public List<Map<String, Object>> getGanttChartByScenario(String scenarioId) {

        List<WorkcenterPlan> plans = workcenterPlanRepository.findAllByScenarioId(scenarioId);

        Map<String, List<WorkcenterPlan>> grouped = new LinkedHashMap<>();
        for (WorkcenterPlan plan : plans) {
            String routingId = plan.getRoutingId();
            if (!grouped.containsKey(routingId)) {
                grouped.put(routingId, new ArrayList<>());
            }
            grouped.get(routingId).add(plan);
        }

        List<Map<String, Object>> result = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        for (Map.Entry<String, List<WorkcenterPlan>> entry : grouped.entrySet()) {
            String routingId = entry.getKey();

            // 상위 Task 객체 추가 (라우팅 단위)
            result.add(Map.of("id", routingId, "name", routingId));

            // 하위 Task 정렬
            entry.getValue().sort(Comparator.comparing(WorkcenterPlan::getWorkcenterStartTime));

            // 하위 Task 객체 생성
            for (WorkcenterPlan plan : entry.getValue()) {
                Map<String, Object> item = new LinkedHashMap<>();
                item.put("id", plan.getOperationId());
                item.put("parentId", plan.getRoutingId());
                item.put("name", plan.getOperationName());
                item.put("workCenter", plan.getWorkcenterName());
                item.put("tool", plan.getToolName());
                item.put("startDate", plan.getWorkcenterStartTime().format(formatter));
                item.put("endDate", plan.getWorkcenterEndTime().format(formatter));

                result.add(item);
            }
        }

        return result;
    }

}
