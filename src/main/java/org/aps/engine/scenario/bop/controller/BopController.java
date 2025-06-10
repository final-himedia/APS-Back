package org.aps.engine.scenario.bop.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.aps.engine.scenario.bop.entity.*;
import org.aps.engine.scenario.bop.repository.*;
import org.aps.engine.scenario.bop.service.BomService;
import org.aps.engine.scenario.bop.service.RoutingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/scenarios/bop")
@RequiredArgsConstructor
public class BopController {

    private final RoutingRepository routingRepository;
    private final OperationRepository operationRepository;
    private final PartRepository partRepository;
    private final BomRepository bomRepository;
    private final SiteRepository siteRepository;
    private final OperationRoutingRepository operationRoutingRepository;
    private final BomService bomService;
    private final RoutingService routingService;


    @GetMapping("/routing")
    public ResponseEntity<?> getAllRouting() {

        List<Routing> routings = routingRepository.findAll();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", 200);
        response.put("routings", routings);
        response.put("total", routings.size());

        return ResponseEntity.status(200).body(response);
    }


    @GetMapping("/operation")
    public ResponseEntity<?> getAllOperation() {
        List<Operation> operations = operationRepository.findAll();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", 200);
        response.put("operations", operations);
        response.put("total", operations.size());

        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/part")
    public ResponseEntity<?> getAllPart() {
        List<Part> parts = partRepository.findAll();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", 200);
        response.put("parts", parts);
        response.put("total", parts.size());

        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/bom")
    public ResponseEntity<?> getAllBom() {
        List<Bom> boms = bomRepository.findAll();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", 200);
        response.put("boms", boms);
        response.put("total", boms.size());

        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/site")
    public ResponseEntity<?> getAllSite() {
        List<Site> sites = siteRepository.findAll();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", 200);
        response.put("sites", sites);
        response.put("total", sites.size());

        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/operationRouting")
    public ResponseEntity<?> getAllOperationRouting() {
        List<OperationRouting> operationRoutings = operationRoutingRepository.findAll();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", 200);
        response.put("operationRoutings", operationRoutings);
        response.put("total", operationRoutings.size());

        return ResponseEntity.status(200).body(response);

    }



    // 🔽 엑셀 업로드: 파일을 받아 DB에 저장
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
