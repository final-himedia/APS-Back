package org.aps.engine.execution.service;

import lombok.RequiredArgsConstructor;
import org.aps.engine.execution.response.ExecutionResponse;
import org.aps.engine.scenario.bop.repository.OperationRepository;
import org.aps.engine.scenario.bop.repository.OperationRoutingRepository;
import org.aps.engine.scenario.resource.repository.ToolMasterRepository;
import org.aps.engine.scenario.resource.repository.WorkCenterMapRepository;
import org.aps.engine.scenario.resource.repository.WorkCenterRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExecutionService {
    private final OperationRoutingRepository operationRoutingRepository;
    private final WorkCenterMapRepository workcenterMapRepository;
    private final ToolMasterRepository toolRepository;
    private final WorkCenterRepository workcenterRepository;
    private final OperationRepository operationRepository;

    public List<ExecutionResponse> executions(String scenarioId){
        List<ExecutionResponse> executionResponses = operationRepository.findByScenarioId(scenarioId);
        return executionResponses;
    }

    public void processExecutions(String scenarioId) {
        List<ExecutionResponse> executions = executions(scenarioId);
        for(ExecutionResponse e : executions) {
            System.out.println(e.getOperationName());
        }
    }





}
