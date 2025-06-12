package org.aps.engine.scenario.resource.controller;

import lombok.RequiredArgsConstructor;
import org.aps.engine.scenario.resource.entity.ToolMaster;
import org.aps.engine.scenario.resource.entity.WorkCenter;
import org.aps.engine.scenario.resource.entity.WorkCenterMap;
import org.aps.engine.scenario.resource.repository.ToolMasterRepository;
import org.aps.engine.scenario.resource.repository.WorkCenterMapRepository;
import org.aps.engine.scenario.resource.repository.WorkCenterRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@CrossOrigin
@RestController
@RequestMapping("/api/scenarios")
@RequiredArgsConstructor
public class ResourceController {

    private final ToolMasterRepository toolMasterRepository;
    private final WorkCenterRepository workCenterRepository;
    private final WorkCenterMapRepository workCenterMapRepository;

    @GetMapping("/resource/tool-master")
    public ResponseEntity<?> getAllToolMaster() {
        List<ToolMaster> toolMasters = toolMasterRepository.findAll();

        Map<String, Object> response = new LinkedHashMap<>();

        response.put("status", 200);
        response.put("toolMasters", toolMasters);
        response.put("total", toolMasters.size());

        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/resource/workcenter")
    public ResponseEntity<?> getAllWorkCenter() {
        List<WorkCenter> workCenters = workCenterRepository.findAll();

        Map<String, Object> response = new LinkedHashMap<>();

        response.put("status", 200);
        response.put("workcenters", workCenters);
        response.put("total", workCenters.size());

        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/resource/workcentermap")
    public ResponseEntity<?> getAllWorkCenterMap() {
        List<WorkCenterMap> workCenterMaps = workCenterMapRepository.findAll();

        Map<String, Object> response = new LinkedHashMap<>();

        response.put("status", 200);
        response.put("workcenterMaps", workCenterMaps);
        response.put("total", workCenterMaps.size());

        return ResponseEntity.status(200).body(response);
    }
}
