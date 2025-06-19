package org.aps.engine.scenario.service;

import lombok.RequiredArgsConstructor;
import org.aps.common.entity.User;
import org.aps.engine.scenario.entity.Scenario;
import org.aps.engine.scenario.respository.ScenarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ScenarioService {
    private final ScenarioRepository scenarioRepository;

    // 전체 목록 조회
    public ResponseEntity<Map<String, Object>> allScenarios() {
        List<Scenario> scenarios = scenarioRepository.findAll();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", 200);
        response.put("scenarios", scenarios);
        response.put("total", scenarios.size());

        return ResponseEntity.status(200).body(response);
    }

    // 시나리오 생성
    public ResponseEntity<Map<String, Object>> createScenario(Scenario scenario, User loginUser) {
        Map<String, Object> response = new LinkedHashMap<>();

        if (loginUser == null) {
            response.put("status", 401);
            response.put("message", "로그인이 필요합니다.");
            return ResponseEntity.status(401).body(response);
        }

        if (scenarioRepository.existsById(scenario.getScenarioId())) {
            response.put("status", 409);
            response.put("message", "이미 등록된 시나리오입니다.");
            return ResponseEntity.status(409).body(response);
        }

        scenarioRepository.save(scenario);

        response.put("status", 200);
        response.put("message", "시나리오 추가 완료.");
        response.put("scenario", scenario);

        return ResponseEntity.status(200).body(response);
    }

    // 시나리오 삭제
    public ResponseEntity<Map<String, Object>> deleteScenario(String scenarioId, User loginUser) {
        Map<String, Object> response = new LinkedHashMap<>();

        if (loginUser == null) {
            response.put("status", 401);
            response.put("message", "로그인이 필요합니다.");
            return ResponseEntity.status(401).body(response);
        }

        if (!scenarioRepository.existsById(scenarioId)) {
            response.put("status", 404);
            response.put("message", "등록되지 않은 시나리오입니다.");
            return ResponseEntity.status(404).body(response);
        }

        scenarioRepository.deleteById(scenarioId);

        response.put("status", 200);
        response.put("message", "시나리오 삭제 완료.");
        response.put("deletedId", scenarioId);

        return ResponseEntity.status(200).body(response);
    }
}
