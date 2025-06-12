package org.aps.engine.scenario.target.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.aps.engine.scenario.target.service.DemandService;
import org.aps.engine.scenario.target.entity.Demand;
import org.aps.engine.scenario.target.repository.DemandRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/scenarios/target")
@RequiredArgsConstructor
public class TargetController {

    private final DemandRepository demandRepository;
    private final DemandService demandService;
    @GetMapping("/demand")
    public ResponseEntity<?> getAllDemand() {

        List<Demand> demands = demandRepository.findAll();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", 200);
        response.put("demands", demands);
        response.put("total", demands.size());

        return ResponseEntity.status(200).body(response);
    }

    @PostMapping("/demand-upload")
    public ResponseEntity<String > uploadDemandExcel(@RequestParam("file") MultipartFile file) throws IOException {
        demandService.excelHandle(file);
        return ResponseEntity.ok("엑셀 업로드 완료");
    }

    @GetMapping("/demand-download")
    private void downloadDemandExcel(HttpServletResponse response) throws IOException {
        demandService.exportDemandExcel(response);
    }
}
