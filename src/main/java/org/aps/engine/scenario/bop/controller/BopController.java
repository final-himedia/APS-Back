package org.aps.engine.scenario.bop.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.aps.engine.scenario.bop.entity.*;
import org.aps.engine.scenario.bop.repository.*;
import org.aps.engine.scenario.bop.service.*;
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

    private final OperationService operationService;
    private final OperationRoutingService operationRoutingService;
    private final PartService partService;
    private final SiteService siteService;


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

    @PostMapping("/bom-save")
    public ResponseEntity<?> saveBom(@RequestBody List<Bom> boms) {
        List<Bom> saved = bomRepository.saveAll(boms);
        return ResponseEntity.ok(Map.of(
                "status", 200,
                "message", "저장 완료"
        ));
    }

    @DeleteMapping("/bom-delete")
    public ResponseEntity<?> deleteBom(@RequestBody List<BomId> ids) {
        bomRepository.deleteAllById(ids);
        return ResponseEntity.ok(Map.of(
                "status", 200,
                "message", "삭제 완료",
                "deletedIds", ids
        ));
    }

    @PostMapping("/operation-save")
    public ResponseEntity<?> saveOperation(@RequestBody List<Operation> operations) {
        List<Operation> saved = operationRepository.saveAll(operations);
        return ResponseEntity.ok(Map.of(
                "status", 200,
                "message", "저장 완료"
        ));
    }

    @DeleteMapping("/operation-delete")
    public ResponseEntity<?> deleteOperation(@RequestBody List<OperationId> ids) {
        operationRepository.deleteAllById(ids);
        return ResponseEntity.ok(Map.of(
                "status", 200,
                "message", "삭제 완료",
                "deletedIds", ids
        ));
    }

    @PostMapping("/part-save")
    public ResponseEntity<?> savePart(@RequestBody List<Part> parts) {
        List<Part> saved = partRepository.saveAll(parts);
        return ResponseEntity.ok(Map.of(
                "status", 200,
                "message", "저장 완료"
        ));
    }

    @DeleteMapping("/part-delete")
    public ResponseEntity<?> deletePart(@RequestBody List<PartId> ids) {
        partRepository.deleteAllById(ids);
        return ResponseEntity.ok(Map.of(
                "status", 200,
                "message", "삭제 완료",
                "deletedIds", ids
        ));
    }

    @PostMapping("/bom-upload")
    public ResponseEntity<String> uploadBomExcel(@RequestParam("file") MultipartFile file) throws IOException {
        bomService.excelHandle(file);
        return ResponseEntity.ok("엑셀 업로드 완료");
    }

    @GetMapping("/bom-download")
    public void downloadBomExcel(HttpServletResponse response) throws IOException {
        bomService.exportBomExcel(response);
    }


    @PostMapping("/operation-upload")
    public ResponseEntity<String > uploadOperationExcel(@RequestParam("file") MultipartFile file) throws IOException {
        operationService.excelHandle(file);
        return ResponseEntity.ok("엑셀 업로드 완료");
    }

    @GetMapping("/operation-download")
    private void downloadOperationExcel(HttpServletResponse response) throws IOException {
        operationService.exportOperationExcel(response);
    }
    @PostMapping("/operation-routing-upload")
    public ResponseEntity<String > uploadOperationRoutingExcel(@RequestParam("file") MultipartFile file) throws IOException {
        operationRoutingService.excelHandle(file);
        return ResponseEntity.ok("엑셀 업로드 완료");
    }

    @GetMapping("/operation-routing-download")
    private void downloadOperationRoutingExcel(HttpServletResponse response) throws IOException {
        operationRoutingService.exportOperationRoutingExcel(response);
    }
    @PostMapping("/part-upload")
    public ResponseEntity<String > uploadPartExcel(@RequestParam("file") MultipartFile file) throws IOException {
        partService.excelHandle(file);
        return ResponseEntity.ok("엑셀 업로드 완료");
    }

    @GetMapping("/part-download")
    private void downloadPartExcel(HttpServletResponse response) throws IOException {
        partService.exportPartExcel(response);
    }
    @PostMapping("/site-upload")
    public ResponseEntity<String > uploadSiteExcel(@RequestParam("file") MultipartFile file) throws IOException {
        siteService.excelHandle(file);
        return ResponseEntity.ok("엑셀 업로드 완료");
    }

    @GetMapping("/site-download")
    private void downloadSiteExcel(HttpServletResponse response) throws IOException {
        siteService.exportSiteExcel(response);
    }
    @PostMapping("/routing-upload")
    public ResponseEntity<String > uploadRoutingExcel(@RequestParam("file") MultipartFile file) throws IOException {
        routingService.routingExcelHandle(file);
        return ResponseEntity.ok("엑셀 업로드 완료");
    }

    @GetMapping("/routing-download")
    private void downloadRoutingExcel(HttpServletResponse response) throws IOException {
        routingService.exportRoutingExcel(response);
    }
}
