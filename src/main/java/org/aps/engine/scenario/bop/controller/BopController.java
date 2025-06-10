package org.aps.engine.scenario.bop.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.aps.engine.scenario.bop.service.BomService;
import org.aps.engine.scenario.bop.service.RoutingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/scenarios/bop")
@RequiredArgsConstructor
public class BopController {

    private final BomService bomService;
    private final RoutingService routingService;

    @PostMapping("/bom-upload")
    public ResponseEntity<String> uploadBomExcel(@RequestParam("file") MultipartFile file) throws IOException {
        bomService.excelHandle(file);
        return ResponseEntity.ok("엑셀 업로드 및 저장 완료");
    }

    @GetMapping("/bom-download")
    public void downloadBomExcel(HttpServletResponse response) throws IOException {
        bomService.exportBomExcel(response);
    }

    @PostMapping("/routing-upload")
    public ResponseEntity<String > uploadRoutingExcel(@RequestParam("file") MultipartFile file) throws IOException {
        routingService.routingExcelHandle(file);
        return ResponseEntity.ok("엑셀 업로드 및 저장 완료");
    }

    @GetMapping("/routing-download")
    private void downloadRoutingExcel(HttpServletResponse response) throws IOException {
        routingService.exportRoutingExcel(response);
    }


}

