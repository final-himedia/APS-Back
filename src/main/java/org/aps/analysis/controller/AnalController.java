package org.aps.analysis.controller;

import lombok.RequiredArgsConstructor;
import org.aps.analysis.entity.Anal;
import org.aps.analysis.repository.AnalRepository;
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
    private final AnalRepository analRepository;

    // 시뮬레이션 실행
    @GetMapping("/get")
    public ResponseEntity<?> runAnal(
            @RequestParam("scenarioId") String scenarioId, @RequestParam("userId") String userId) {
        Anal saveAnal = analService.runSimulationAndSaveAnal(scenarioId, userId);
        return ResponseEntity.ok(saveAnal);
    }

    // 간트차트 조회
    @PostMapping("/gantt")
    public ResponseEntity<?> getGanttChart(@RequestBody AnalRequest request) {
        List<Map<String, Object>> ganttData = executionResultService.getGanttChartByCondition(request);

        if (ganttData.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(ganttData);
    }

    @GetMapping("get/{scenarioId}")
    public ResponseEntity<?> getAnalByScenarioId(@PathVariable("scenarioId") String s){
        List<Anal> anals = analRepository.findAllByScenarioId(s);
        return ResponseEntity.ok(anals);

    }

}
