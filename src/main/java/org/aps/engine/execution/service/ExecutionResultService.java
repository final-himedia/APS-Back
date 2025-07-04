package org.aps.engine.execution.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.aps.analysis.repository.AnalRepository;
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

        // 1. Tool 초기화
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

        // 3. OperationRoute 가져오기
        List<OperationRoute> operationRoutes = operationRouteRepository.findById_ScenarioIdOrderByOperationSeq(scenarioId);
        Map<String, LocalDateTime> lastEndTimeByRouting = new HashMap<>();

        for (OperationRoute route : operationRoutes) {
            String routingId = route.getId().getRoutingId();

            List<WorkCenterMap> wcMaps = workCenterMapRepository
                    .findByOperationRoute_Id_RoutingIdAndOperationRoute_Id_ScenarioId(routingId, scenarioId);

            if (wcMaps == null || wcMaps.isEmpty()) {
                System.out.println("[WARN] Skipping route because no WorkCenterMap: routingId=" + routingId);
                continue;
            }

            String selectedWorkcenterId = null;
            WorkCenterMap selectedWcMap = null;
            LocalDateTime selectedStart = null;

            // ✅ 가장 빠른 워크센터 중 랜덤 선택
            LocalDateTime earliestWcReady = null;
            List<String> earliestWcIds = new ArrayList<>();
            Map<String, WorkCenterMap> wcMapById = new HashMap<>();

            for (WorkCenterMap wcMap : wcMaps) {
                String wcId = wcMap.getWorkCenter().getWorkCenterId().getWorkcenterId();
                LocalDateTime wcReady = workcenterAvailableTime.getOrDefault(wcId, startTime);
                LocalDateTime prevOpEnd = lastEndTimeByRouting.getOrDefault(routingId, startTime);
                LocalDateTime candidateStart = wcReady.isAfter(prevOpEnd) ? wcReady : prevOpEnd;

                if (earliestWcReady == null || candidateStart.isBefore(earliestWcReady)) {
                    earliestWcReady = candidateStart;
                    earliestWcIds.clear();
                    earliestWcIds.add(wcId);
                } else if (candidateStart.equals(earliestWcReady)) {
                    earliestWcIds.add(wcId);
                }
                wcMapById.put(wcId, wcMap);
            }

            Random random = new Random();
            selectedWorkcenterId = earliestWcIds.get(random.nextInt(earliestWcIds.size()));
            selectedStart = earliestWcReady;
            selectedWcMap = wcMapById.get(selectedWorkcenterId);

            // ✅ 툴 선택: 가장 이른 툴 중 랜덤 선택
            LocalDateTime earliestToolReady = null;
            List<String> earliestTools = new ArrayList<>();
            for (String toolId : toolAvailableTime.keySet()) {
                LocalDateTime toolReady = toolAvailableTime.get(toolId);
                if (earliestToolReady == null || toolReady.isBefore(earliestToolReady)) {
                    earliestToolReady = toolReady;
                    earliestTools.clear();
                    earliestTools.add(toolId);
                } else if (toolReady.equals(earliestToolReady)) {
                    earliestTools.add(toolId);
                }
            }
            String selectedToolId = earliestTools.get(random.nextInt(earliestTools.size()));
            LocalDateTime toolReady = toolAvailableTime.get(selectedToolId);

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

            workcenterAvailableTime.put(selectedWorkcenterId, unitEnd);
            toolAvailableTime.put(selectedToolId, unitEnd);
            lastEndTimeByRouting.put(routingId, unitEnd);
        }

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

    public List<Map<String, Object>> getTop5Operations(String scenarioId) {
        List<WorkcenterPlan> plans = workcenterPlanRepository.findAllByScenarioId(scenarioId);

        return plans.stream().map(plan -> {
                    long durationMinutes = Duration.between(
                            plan.getWorkcenterStartTime(),
                            plan.getWorkcenterEndTime()
                    ).toMinutes();

                    Map<String, Object> item = new HashMap<>();
                    item.put("routingId", plan.getRoutingId());
                    item.put("operationId", plan.getOperationId());
                    item.put("operationName", plan.getOperationName());
                    item.put("durationMinutes", durationMinutes);
                    item.put("workcenter", plan.getWorkcenterName());
                    return item;
                })
                .sorted((a, b) -> Long.compare(
                        (Long) b.get("durationMinutes"),
                        (Long) a.get("durationMinutes")
                ))
                .limit(5)
                .collect(Collectors.toList());
    }



    public List<Map<String, Object>> getTotalTimeRouting(String scenarioId) {
        List<WorkcenterPlan> plans = workcenterPlanRepository.findAllByScenarioId(scenarioId);

        Map<String, Long> totalDurationPerRouting = new HashMap<>();

        for (WorkcenterPlan plan : plans) {
            long durationMinutes = Duration.between(
                    plan.getWorkcenterStartTime(),
                    plan.getWorkcenterEndTime()
            ).toMinutes();

            String routingId = plan.getRoutingId();

            totalDurationPerRouting.put(
                    routingId,
                    totalDurationPerRouting.getOrDefault(routingId, 0L) + durationMinutes
            );
        }

        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<String, Long> entry : totalDurationPerRouting.entrySet()) {
            Map<String, Object> map = new HashMap<>();
            map.put("routingId", entry.getKey());
            map.put("totalDurationMinutes", entry.getValue());
            result.add(map);
        }

        return result;
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
