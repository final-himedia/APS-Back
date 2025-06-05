package org.aps.engine.execution.controller;

import lombok.RequiredArgsConstructor;
import org.aps.engine.execution.response.OperationSchedule;
import org.aps.engine.execution.service.SimulateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/execution")
@RequiredArgsConstructor
public class ExecutionController {
    private final SimulateService simulateService;

    @GetMapping("/simulate")
    public ResponseEntity<?> simulateHandle() {

        List<OperationSchedule> schedules
                = simulateService.simulateSequentialSchedule();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", 200);
        response.put("result", schedules);
        response.put("message", "successfully scheduled");


        return ResponseEntity.status(200).body(response);
    }
}
