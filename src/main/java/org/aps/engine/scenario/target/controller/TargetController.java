package org.aps.engine.scenario.target.controller;

import lombok.RequiredArgsConstructor;
import org.aps.engine.scenario.bop.entity.Routing;
import org.aps.engine.scenario.target.entity.Demand;
import org.aps.engine.scenario.target.repository.DemandRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/scenarios/target")
@RequiredArgsConstructor
public class TargetController {

    private final DemandRepository demandRepository;

    @GetMapping("/demand")
    public ResponseEntity<?> getAllDemand() {

        List<Demand> demands = demandRepository.findAll();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", 200);
        response.put("demands", demands);
        response.put("total", demands.size());

        return ResponseEntity.status(200).body(response);
    }
}
