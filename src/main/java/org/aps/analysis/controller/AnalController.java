package org.aps.analysis.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.aps.analysis.entity.Anal;
import org.aps.analysis.repository.AnalRepository;
import org.aps.analysis.service.AnalService;
import org.aps.engine.execution.service.ExecutionResultService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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
    public ResponseEntity<?> getAnalByScenarioId(@PathVariable("scenarioId") String s) {
        List<Anal> anals = analRepository.findAllByScenarioId(s);
        return ResponseEntity.ok(anals);

    }

    // 실행 이력 전체 목록 조회
    @GetMapping("/list")
    public ResponseEntity<?> getAllAnal() {
        List<Anal> allAnal = analRepository.findAllByOrderByIdDesc();
        return ResponseEntity.ok(allAnal);
    }

    // 실행 이력 다운로드
    @GetMapping("/download")
    public void downloadAnalExcel(@RequestParam("scenarioId") String scenarioId, HttpServletResponse response) throws IOException {
        analService.exportAnalExcel(scenarioId, response);
    }

    // 실행 이력 삭제
    @DeleteMapping("/delete/{scenarioId}")
    public ResponseEntity<?> deleteAnal(@PathVariable("scenarioId") String scenarioId) {
        List<Anal> delete = analRepository.findAllByScenarioId(scenarioId);
        analRepository.deleteAll(delete);
        return ResponseEntity.ok(delete);
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

