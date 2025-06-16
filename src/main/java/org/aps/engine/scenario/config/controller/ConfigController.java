package org.aps.engine.scenario.config.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.aps.engine.scenario.bop.entity.Routing;
import org.aps.engine.scenario.config.entity.Priority;
import org.aps.engine.scenario.config.repository.PriorityRepository;
import org.aps.engine.scenario.config.service.ConfigService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/scenarios/config")
@RequiredArgsConstructor
public class ConfigController {

    private final PriorityRepository priorityRepository;
    private final ConfigService configService;

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

    @PostMapping("/priority-upload")
    public ResponseEntity<String > uploadPriorityExcel(@RequestParam("file") MultipartFile file) throws IOException {
        configService.excelHandle(file);
        return ResponseEntity.ok("엑셀 업로드 완료");
    }

    @GetMapping("/priority-download")
    private void downloadPriorityExcel(HttpServletResponse response) throws IOException {
        configService.exportPriorityExcel(response);
    }
}
