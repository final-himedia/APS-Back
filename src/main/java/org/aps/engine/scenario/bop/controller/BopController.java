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
    private final PartRepository partRepository;
    private final BomRepository bomRepository;
    private final SiteRepository siteRepository;
    private final BomService bomService;
    private final RoutingService routingService;
    private final PartService partService;
    private final SiteService siteService;
    private final OperationRouteRepository operationRouteRepository;
    private final OperationRouteService operationRouteService;



    // 시나리오 ID 별로 조회
    @GetMapping("/routing/{scenarioId}")
    public ResponseEntity<?> getAllRouting(@PathVariable String scenarioId) {
        List<Routing> routings = routingRepository.findByRoutingIdScenarioId(scenarioId);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", 200);
        response.put("routings", routings);
        response.put("total", routings.size());

        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/part/{scenarioId}")
    public ResponseEntity<?> getAllPart(@PathVariable String scenarioId) {
        List<Part> parts = partRepository.findByPartIdScenarioId(scenarioId);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", 200);
        response.put("parts", parts);
        response.put("total", parts.size());

        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/bom/{scenarioId}")
    public ResponseEntity<?> getAllBom(@PathVariable String scenarioId) {
        List<Bom> boms = bomRepository.findByBomIdScenarioId(scenarioId);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", 200);
        response.put("boms", boms);
        response.put("total", boms.size());

        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/site/{scenarioId}")
    public ResponseEntity<?> getAllSite(@PathVariable String scenarioId) {
        List<Site> sites = siteRepository.findByScenarioId(scenarioId);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", 200);
        response.put("sites", sites);
        response.put("total", sites.size());

        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/operationRoute/{scenarioId}")
    public ResponseEntity<?> getAllOperationRoute(@PathVariable String scenarioId) {
        List<OperationRoute> operationRoutes = operationRouteRepository.findById_ScenarioId(scenarioId);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", 200);
        response.put("operationRoute", operationRoutes);
        response.put("total", operationRoutes.size());

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

    @PostMapping("/operationRoute-save")
    public ResponseEntity<?> saveOperationRoute(@RequestBody List<OperationRoute> operationRoutes) {
        List<OperationRoute> saved = operationRouteRepository.saveAll(operationRoutes);
        return ResponseEntity.ok(Map.of(
                "status", 200,
                "message", "저장 완료"
        ));
    }

    @DeleteMapping("/operationRoute-delete")
    public ResponseEntity<?> deleteOperationRouteByIdList(@RequestBody List<OperationRouteId> ids) {
        operationRouteRepository.deleteAllById(ids);
        return ResponseEntity.ok(Map.of(
                "status", 200,
                "message", "삭제 완료",
                "deletedIds", ids
        ));
    }

    @PostMapping("/bom-upload")
    public ResponseEntity<String> uploadBomExcel(@RequestParam("file") MultipartFile file, @RequestParam("scenarioId") String scenarioId) throws IOException {
        bomService.excelHandle(file, scenarioId);
        return ResponseEntity.ok("엑셀 업로드 완료");
    }

    @GetMapping("/bom-download")
    public void downloadBomExcel(@RequestParam("scenarioId") String scenarioId, HttpServletResponse response) throws IOException {
        bomService.exportBomExcel(scenarioId, response);
    }


    @PostMapping("/part-upload")
    public ResponseEntity<String > uploadPartExcel(@RequestParam("file") MultipartFile file,@RequestParam("scenarioId") String scenarioId) throws IOException {
        partService.excelHandle(file, scenarioId);
        return ResponseEntity.ok("엑셀 업로드 완료");
    }

    @GetMapping("/part-download")
    private void downloadPartExcel(@RequestParam("scenarioId") String scenarioId, HttpServletResponse response) throws IOException {
        partService.exportPartExcel(scenarioId,response);
    }
    @PostMapping("/site-upload")
    public ResponseEntity<String > uploadSiteExcel(@RequestParam("file") MultipartFile file,@RequestParam("scenarioId") String scenarioId) throws IOException {
        siteService.excelHandle(file,scenarioId);
        return ResponseEntity.ok("엑셀 업로드 완료");
    }

    @GetMapping("/site-download")
    private void downloadSiteExcel(@RequestParam("scenarioId") String scenarioId,HttpServletResponse response) throws IOException {
        siteService.exportSiteExcel(scenarioId,response);
    }
    @PostMapping("/routing-upload")
    public ResponseEntity<String > uploadRoutingExcel(@RequestParam("file") MultipartFile file,@RequestParam("scenarioId") String scenarioId) throws IOException {
        routingService.routingExcelHandle(file,scenarioId);
        return ResponseEntity.ok("엑셀 업로드 완료");
    }

    @GetMapping("/routing-download")
    private void downloadRoutingExcel(@RequestParam("scenarioId") String scenarioId,HttpServletResponse response) throws IOException {
        routingService.exportRoutingExcel(scenarioId,response);
    }

    @PostMapping("/operationRoute-upload")
    public ResponseEntity<String > uploadOperationRouteExcel(@RequestParam("file") MultipartFile file,@RequestParam("scenarioId") String scenarioId) throws IOException {
        operationRouteService.operationRouteExcelHandle(file,scenarioId);
        return ResponseEntity.ok("엑셀 업로드 완료");
    }

    @GetMapping("/operationRoute-download")
    private void downloadOperationRouteExcel(@RequestParam("scenarioId") String scenarioId,HttpServletResponse response) throws IOException {
        operationRouteService.exportOperationRouteExcel(scenarioId,response);
    }
}
