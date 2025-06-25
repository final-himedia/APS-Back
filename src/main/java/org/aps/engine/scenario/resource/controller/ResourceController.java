//package org.aps.engine.scenario.resource.controller;
//
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.aps.engine.scenario.resource.entity.ToolMap;
//import org.aps.engine.scenario.resource.entity.ToolMaster;
//import org.aps.engine.scenario.resource.entity.WorkCenter;
//import org.aps.engine.scenario.resource.entity.WorkCenterMap;
//import org.aps.engine.scenario.resource.repository.ToolMapRepository;
//import org.aps.engine.scenario.resource.repository.ToolMasterRepository;
//import org.aps.engine.scenario.resource.repository.WorkCenterMapRepository;
//import org.aps.engine.scenario.resource.repository.WorkCenterRepository;
//import org.aps.engine.scenario.resource.service.ToolMapService;
//import org.aps.engine.scenario.resource.service.ToolMasterService;
//import org.aps.engine.scenario.resource.service.WorkCenterMapService;
//import org.aps.engine.scenario.resource.service.WorkCenterService;
//import org.springframework.http.ResponseEntity;
//
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//
//import java.io.IOException;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//
//@CrossOrigin
//@RestController
//@RequestMapping("/api/scenarios/resource")
//@RequiredArgsConstructor
//public class ResourceController {
//
//    private final ToolMasterRepository toolMasterRepository;
//    private final WorkCenterRepository workCenterRepository;
//    private final WorkCenterMapRepository workCenterMapRepository;
//    private final ToolMasterService toolMasterService;
//    private final WorkCenterService workCenterService;
//    private final WorkCenterMapService workCenterMapService;
//    private final ToolMapService toolMapService;
//    private final ToolMapRepository toolMapRepository;
//
//    @GetMapping("/tool-master/{scenarioId}")
//    public ResponseEntity<?> getAllToolMaster(@PathVariable String scenarioId) {
//        List<ToolMaster> toolMasters = toolMasterRepository.findAllToolMastersByScenarioId(scenarioId);
//
//        Map<String, Object> response = new LinkedHashMap<>();
//
//        response.put("status", 200);
//        response.put("toolMasters", toolMasters);
//        response.put("total", toolMasters.size());
//
//        return ResponseEntity.status(200).body(response);
//    }
//    @GetMapping("/tool-map/{scenarioId}")
//    public ResponseEntity<?> getAllToolMap(@PathVariable String scenarioId) {
//        List<ToolMap> toolMaps = toolMapRepository.findByScenarioId(scenarioId);
//
//        Map<String, Object> response = new LinkedHashMap<>();
//
//        response.put("status", 200);
//        response.put("toolMasters", toolMaps);
//        response.put("total", toolMaps.size());
//
//        return ResponseEntity.status(200).body(response);
//    }
//
//    @GetMapping("/workcenter/{scenarioId}")
//    public ResponseEntity<?> getAllWorkCenter(@PathVariable String scenarioId) {
//        List<WorkCenter> workCenters = workCenterRepository.findByWorkCenterIdScenarioId(scenarioId);
//
//        Map<String, Object> response = new LinkedHashMap<>();
//
//        response.put("status", 200);
//        response.put("workcenters", workCenters);
//        response.put("total", workCenters.size());
//
//        return ResponseEntity.status(200).body(response);
//    }
//
//    @GetMapping("/workcentermap/{scenarioId}")
//    public ResponseEntity<?> getAllWorkCenterMap(@PathVariable String scenarioId) {
//        List<WorkCenterMap> workCenterMaps = workCenterMapRepository.findByWorkCenterScenarioId(scenarioId);
//
//        Map<String, Object> response = new LinkedHashMap<>();
//
//        response.put("status", 200);
//        response.put("workcenterMaps", workCenterMaps);
//        response.put("total", workCenterMaps.size());
//
//        return ResponseEntity.status(200).body(response);
//    }
//
//    @PostMapping("/tool-upload")
//    public ResponseEntity<String > uploadToolExcel(@RequestParam("file") MultipartFile file, @RequestParam("scenarioId") String s) throws IOException {
//        toolMasterService.excelHandle(file,s);
//        return ResponseEntity.ok("엑셀 업로드 완료");
//    }
//
//    @GetMapping("/tool-download")
//    private void downloadToolExcel(HttpServletResponse response, @RequestParam("scenarioId") String s) throws IOException {
//        toolMasterService.exportToolExcel(s,response);
//    }
//    @PostMapping("/workcenter-upload")
//    public ResponseEntity<String > uploadWorkcenterExcel(@RequestParam("file") MultipartFile file, @RequestParam("scenarioId") String s) throws IOException {
//        workCenterService.excelHandle(file,s);
//        return ResponseEntity.ok("엑셀 업로드 완료");
//    }
//
//    @GetMapping("/workcenter-download")
//    private void downloadWorkcenterExcel(HttpServletResponse response, @RequestParam("scenarioId") String s) throws IOException {
//        workCenterService.exportWorkCenterExcel(s,response);
//    }
//    @PostMapping("/workcentermap-upload")
//    public ResponseEntity<String > uploadWorkcenterMapExcel(@RequestParam("file") MultipartFile file, @RequestParam("scenarioId") String s) throws IOException {
//        workCenterMapService.excelHandle(file,s);
//        return ResponseEntity.ok("엑셀 업로드 완료");
//    }
//
//    @GetMapping("/workcentermap-download")
//    private void downloadWorkcenterMapExcel(HttpServletResponse response, @RequestParam("scenarioId") String s) throws IOException {
//        workCenterMapService.exportWorkCenterMapExcel(s,response);
//    }
//
//    @PostMapping("/toolmap-upload")
//    public ResponseEntity<String > uploadToolMapExcel(@RequestParam("file") MultipartFile file, @RequestParam("scenarioId") String s) throws IOException {
//        toolMapService.excelHandle(file,s);
//        return ResponseEntity.ok("엑셀 업로드 완료");
//    }
//
//    @GetMapping("/toolmap-download")
//    private void downloadToolMapExcel(HttpServletResponse response, @RequestParam("scenarioId") String s) throws IOException {
//        toolMapService.exportToolMapExcel(s,response);
//    }
//
//}
