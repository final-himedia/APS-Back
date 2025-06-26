package org.aps.analysis.controller;

import lombok.RequiredArgsConstructor;
import org.aps.analysis.entity.Anal;
import org.aps.analysis.request.AnalRequest;
import org.aps.analysis.service.AnalService;
import org.aps.engine.execution.service.ExecutionResultService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/analysis")
@RequiredArgsConstructor
public class AnalController {
    private final AnalService analService;
    private final ExecutionResultService executionResultService;

    // 시뮬레이션 실행
    @GetMapping()
    public ResponseEntity<?> runAnal(
            @RequestParam String scenarioId, @RequestParam String userId) {
        Anal saveAnal = analService.runSimulationAndSaveAnal(scenarioId, userId);
        return ResponseEntity.ok(saveAnal);
    }

    // 간트차트 조회
    @GetMapping("/gantt")
    public ResponseEntity<List<Map<String, Object>>> getGanttChart(@RequestParam String scenarioId) {
        List<Map<String, Object>> ganttData = executionResultService.getGanttChartByScenario(scenarioId);

        if (ganttData.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(ganttData);
    }
}