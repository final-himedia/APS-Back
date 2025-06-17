package org.aps.engine.execution.service;

import lombok.RequiredArgsConstructor;
import org.aps.engine.execution.response.ExecutionResponse;
import org.aps.engine.execution.result.ExecutionResult;
import org.aps.engine.scenario.bop.repository.OperationRepository;
import org.aps.engine.scenario.bop.repository.OperationRoutingRepository;
import org.aps.engine.scenario.resource.entity.WorkCenter;
import org.aps.engine.scenario.resource.repository.ToolMasterRepository;
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

    private final OperationRepository operationRepository;
    private final OperationRoutingRepository operationRoutingRepository;
    private final ToolMasterRepository toolRepository;
    private final WorkCenterRepository workcenterRepository;

    public List<ExecutionResult> simulateSchedule(String scenarioId) {
        List<ExecutionResponse> operations = operationRepository.findByScenarioId(scenarioId);

        LocalDateTime baseStartTime = LocalDateTime.of(2025, 6, 4, 9, 0);

        Map<String, LocalDateTime> toolAvailableTime = new HashMap<>();
        toolRepository.findAll().forEach(tool ->
                toolAvailableTime.put(tool.getToolMasterId().getToolId(), baseStartTime)
        );

        Map<String, LocalDateTime> workcenterAvailableTime = new HashMap<>();
        workcenterRepository.findAll().forEach(wc ->
                workcenterAvailableTime.put(wc.getWorkCenterId().getWorkcenterId(), baseStartTime)
        );

        List<ExecutionResult> results = new ArrayList<>();

        for (ExecutionResponse op : operations) {
            List<WorkCenter> candidateWorkcenters = workcenterRepository.findByWorkCenterIdScenarioId(op.getOperationId());

            if (candidateWorkcenters == null || candidateWorkcenters.isEmpty()) {
                // 스킵하거나 로그 처리
                continue;
            }

            WorkCenter selectedWorkcenter = null;
            LocalDateTime earliestWcTime = null;

            for (WorkCenter wc : candidateWorkcenters) {
                LocalDateTime wcTime = workcenterAvailableTime.getOrDefault(wc.getWorkCenterId().getWorkcenterId(), baseStartTime);
                if (earliestWcTime == null || wcTime.isBefore(earliestWcTime)) {
                    selectedWorkcenter = wc;
                    earliestWcTime = wcTime;
                }
            }

            String selectedToolId = null;
            LocalDateTime earliestToolTime = null;

            for (Map.Entry<String, LocalDateTime> entry : toolAvailableTime.entrySet()) {
                String toolId = entry.getKey();
                LocalDateTime toolTime = entry.getValue();
                if (earliestToolTime == null || toolTime.isBefore(earliestToolTime)) {
                    selectedToolId = toolId;
                    earliestToolTime = toolTime;
                }
            }

            if (earliestToolTime == null || earliestWcTime == null || selectedWorkcenter == null || selectedToolId == null) {
                // 잘못된 매핑이 있는 경우 스킵
                continue;
            }

            LocalDateTime startTime = earliestToolTime.isAfter(earliestWcTime) ? earliestToolTime : earliestWcTime;

            int runTimeMinutes;
            try {
                runTimeMinutes = Integer.parseInt(op.getRunTime());
            } catch (NumberFormatException e) {
                runTimeMinutes = 10; // 기본값 or continue;
            }

            LocalDateTime endTime = startTime.plusMinutes(runTimeMinutes);

            toolAvailableTime.put(selectedToolId, endTime);
            workcenterAvailableTime.put(selectedWorkcenter.getWorkCenterId().getWorkcenterId(), endTime);

            ExecutionResult dto = new ExecutionResult();
            dto.setScenarioId(scenarioId);
            dto.setOperationId(op.getOperationId());
            dto.setOperationName(op.getOperationName());
            dto.setWorkcenterId(selectedWorkcenter.getWorkCenterId().getWorkcenterId());
            dto.setWorkcenterName(selectedWorkcenter.getWorkcenterName());
            dto.setWorkcenterStartTime((int) java.time.Duration.between(baseStartTime, startTime).toMinutes());
            dto.setWorkcenterEndTime(endTime);
            dto.setProcessQty(op.getOrOperationSeq());
            dto.setToolDetail1(selectedToolId);
            // 필요 시 추가 필드 설정

            results.add(dto);
        }

        return results;
    }
}
