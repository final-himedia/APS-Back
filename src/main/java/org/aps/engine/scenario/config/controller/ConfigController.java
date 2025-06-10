package org.aps.engine.scenario.config.controller;

import lombok.RequiredArgsConstructor;
import org.aps.engine.scenario.bop.entity.Routing;
import org.aps.engine.scenario.config.entity.Priority;
import org.aps.engine.scenario.config.repository.PriorityRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/scenarios/config")
@RequiredArgsConstructor
public class ConfigController {

    private final PriorityRepository priorityRepository;

    @GetMapping("/priority")
    public ResponseEntity<?> getAllPriority() {

        List<Priority> prioritys = priorityRepository.findAll();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", 200);
        response.put("prioritys", prioritys);
        response.put("total", prioritys.size());
        response.put("success", true);

        return ResponseEntity.status(200).body(response);
    }
}
