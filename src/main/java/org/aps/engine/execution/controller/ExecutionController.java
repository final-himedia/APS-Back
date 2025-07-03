package org.aps.engine.execution.controller;

import lombok.RequiredArgsConstructor;
import org.aps.engine.execution.repository.WorkcenterPlanRepository;
import org.aps.engine.execution.result.WorkcenterPlan;
import org.aps.engine.execution.service.ExecutionResultService;
import org.aps.engine.scenario.bop.entity.OperationRoute;
import org.aps.engine.scenario.resource.entity.ToolMaster;
import org.aps.engine.scenario.resource.entity.WorkCenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/execution")
@RequiredArgsConstructor
public class ExecutionController {
    private final ExecutionResultService executionResultService;
    private final WorkcenterPlanRepository workcenterPlanRepository;

    @GetMapping
    public ResponseEntity<?> executionSimulate(@RequestParam("scenarioId")String s){
       executionResultService.simulateSchedule(s);
       return ResponseEntity.ok(s);
    }

    @GetMapping("/get")
    public ResponseEntity<?> executeGet(@RequestParam("scenarioId") String s){
        List<Map<String, Object>> result = executionResultService.getExecutionResult(s);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/operation")
    public ResponseEntity<?> getOperation(@RequestParam("scenarioId") String scenarioId) {
        List<OperationRoute> routes = executionResultService.getRoutesByScenario(scenarioId);
        return ResponseEntity.ok(routes);
    }

    @GetMapping("/workcenter")
    public ResponseEntity<?> getWorkcenter(@RequestParam("scenarioId") String scenarioId) {
        List<WorkCenter> workcenter = executionResultService.getWorkcenterByScenario(scenarioId);
        return ResponseEntity.ok(workcenter);
    }

    @GetMapping("/tool")
    public ResponseEntity<?> getTool(@RequestParam("scenarioId")String s){
        List<ToolMaster> toolMasters = executionResultService.getUsedToolsByScenario(s);
        return ResponseEntity.ok(toolMasters);
    }

    @GetMapping("/top5-operation")
    public List<Map<String, Object>> getTopOperation(@RequestParam("scenarioId")String s){
        return executionResultService.getTop5Operations(s);
    }

    @GetMapping("/total-time-routing")
    public List<Map<String, Object>> getTotalTimeRouting(@RequestParam("scenarioId")String s){
        return executionResultService.getTotalTimeRouting(s);
    }
}
