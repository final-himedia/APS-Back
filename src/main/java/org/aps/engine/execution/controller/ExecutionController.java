package org.aps.engine.execution.controller;

import lombok.RequiredArgsConstructor;
import org.aps.engine.execution.service.ExecutionResultService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/execution")
@RequiredArgsConstructor
public class ExecutionController {
    private final ExecutionResultService executionResultService;

    @GetMapping
    public ResponseEntity<?> executionTest(@RequestParam("scenarioId")String s){
       executionResultService.simulateSchedule(s);
       return ResponseEntity.ok(s);
    }
}
