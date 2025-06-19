package org.aps.engine.scenario.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aps.common.entity.User;
import org.aps.engine.scenario.entity.Scenario;
import org.aps.engine.scenario.service.ScenarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/scenarios")
@RequiredArgsConstructor
public class ScenarioController {
    private final ScenarioService scenarioService;

    // 시나리오 전체 목록 조회
    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> allScenarios() {
        return scenarioService.allScenarios();
    }

    // 시나리오 생성
    @PostMapping
    public ResponseEntity<Map<String, Object>> createScenario(@RequestBody Scenario scenario,
                                                              HttpServletRequest request) {
        User loginUser = (User) request.getAttribute("user");
        return scenarioService.createScenario(scenario, loginUser);
    }

    // 시나리오 삭제
    @DeleteMapping("/{scenarioId}")
    public ResponseEntity<Map<String, Object>> deleteScenario(@PathVariable String scenarioId,
                                                              HttpServletRequest request) {
        User loginUser = (User) request.getAttribute("user");
        return scenarioService.deleteScenario(scenarioId, loginUser);
    }
}
