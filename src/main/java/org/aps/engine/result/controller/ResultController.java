package org.aps.engine.result.controller;

import lombok.RequiredArgsConstructor;
import org.aps.engine.result.entity.WorkcenterPlan;
import org.aps.engine.result.repository.WorkcenterPlanRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/result")
@RequiredArgsConstructor
public class ResultController {
    private final WorkcenterPlanRepository workcenterPlanRepository;

    // 시나리오 ID 별로 조회
    @GetMapping("/workcenter-plan/{scenarioId}")
    public ResponseEntity<?> workcenterPlan(@PathVariable String scenarioId) {
        List<WorkcenterPlan> plans = workcenterPlanRepository.findByScenarioId(scenarioId);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", 200);
        response.put("plans", plans);
        response.put("total", plans.size());

        return ResponseEntity.status(200).body(response);
    }
}

