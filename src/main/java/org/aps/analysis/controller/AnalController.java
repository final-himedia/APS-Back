package org.aps.analysis.controller;

import lombok.RequiredArgsConstructor;
import org.aps.analysis.entity.Anal;
import org.aps.analysis.repository.AnalRepository;
import org.aps.analysis.service.AnalService;
import org.aps.engine.execution.service.ExecutionResultService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/analysis")
@RequiredArgsConstructor
public class AnalController {
    private final AnalService analService;
    private final ExecutionResultService executionResultService;
    private final AnalRepository analRepository;

    // 시뮬레이션 실행
    @GetMapping("/get")
    public ResponseEntity<?> runAnal(
            @RequestParam("scenarioId") String scenarioId, @RequestParam("userId") String userId) {
        Anal saveAnal = analService.runSimulationAndSaveAnal(scenarioId, userId);
        return ResponseEntity.ok(saveAnal);
    }

    @GetMapping("get/{scenarioId}")
    public ResponseEntity<?> getAnalByScenarioId(@PathVariable("scenarioId") String s){
        List<Anal> anals = analRepository.findAllByScenarioId(s);
        return ResponseEntity.ok(anals);

    }

    // 실행 이력 전체 목록 조회
    @GetMapping("/list")
    public ResponseEntity<?> getAllAnal() {
        List<Anal> allAnal = analService.getAllAnal();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", 200);
        response.put("analList", allAnal);
        response.put("total", allAnal.size());

        return ResponseEntity.ok(response);
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

