package org.aps.engine.result.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.aps.engine.execution.repository.WorkcenterPlanRepository;
import org.aps.engine.execution.result.WorkcenterPlan;
import org.aps.engine.result.service.ResultService;
import org.aps.engine.scenario.service.ScenarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/result")
@RequiredArgsConstructor
public class ResultController {

    private final WorkcenterPlanRepository workcenterPlanRepository;
    private final ScenarioService scenarioService;
    private final ResultService resultService;

    // 시나리오 전체 목록 조회
    @GetMapping("/scenarios")
    public ResponseEntity<?> scenarioList() {
        return scenarioService.allScenarios();
    }

    // 시나리오 ID 별로 조회
    @GetMapping("/workcenter-plan/{scenarioId}")
    public ResponseEntity<?> workcenterPlan(@PathVariable String scenarioId) {
        List<WorkcenterPlan> plans = workcenterPlanRepository.findAllByScenarioId(scenarioId);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", 200);
        response.put("plans", plans);
        response.put("total", plans.size());

        return ResponseEntity.status(200).body(response);
    }

    // 엑셀 다운로드
    @GetMapping("/workcenter-plan/download")
    public void downloadExcel(@RequestParam("scenarioId") String scenarioId, HttpServletResponse response) throws IOException {
        resultService.excelHandle(scenarioId, response);
    }
}
