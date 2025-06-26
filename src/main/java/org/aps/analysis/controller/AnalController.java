package org.aps.analysis.controller;

import lombok.RequiredArgsConstructor;
import org.aps.analysis.entity.Anal;
import org.aps.analysis.service.AnalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analysis")
@RequiredArgsConstructor
public class AnalController {
    private final AnalService analService;

    @GetMapping()
    public ResponseEntity<?> runAnal(
            @RequestParam String scenarioId, @RequestParam String userId) {
        Anal saveAnal = analService.runSimulationAndSaveAnal(scenarioId, userId);
        return ResponseEntity.ok(saveAnal);
    }
}
