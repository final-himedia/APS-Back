//package org.aps.engine.scenario.resource.service;
//
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.apache.poi.ss.formula.functions.T;
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.aps.engine.scenario.bop.entity.Operation;
//import org.aps.engine.scenario.bop.entity.OperationId;
//import org.aps.engine.scenario.resource.entity.ToolMaster;
//import org.aps.engine.scenario.resource.entity.ToolMasterId;
//import org.aps.engine.scenario.resource.repository.ToolMasterRepository;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class ToolMasterService {
//    private final ToolMasterRepository toolMasterRepository;
//
//    public void excelHandle(MultipartFile file, String scenarioId) throws IOException {
//        DataFormatter formatter = new DataFormatter();
//        Workbook workbook = WorkbookFactory.create(file.getInputStream());
//        Sheet sheet = workbook.getSheetAt(0);
//
//        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
//            Row row = sheet.getRow(i);
//
//            String siteId = row.getCell(0) == null ? "" : formatter.formatCellValue(row.getCell(0));
//            String toolId = row.getCell(1) == null ? "" : formatter.formatCellValue(row.getCell(1));
//
//            ToolMasterId toolMasterId = ToolMasterId.builder()
//                    .siteId(siteId)
//                    .toolId(toolId)
//                    .scenarioId(scenarioId)
//                    .build();
//
//            String cavityStr = row.getCell(3) == null ? "" : formatter.formatCellValue(row.getCell(3));
//            Integer cavity = cavityStr.isEmpty() ? 0 : Integer.valueOf(cavityStr);
//
//            String toolState = row.getCell(2) == null ? "" : formatter.formatCellValue(row.getCell(2));
//            String toolName = row.getCell(5) == null ? "" : formatter.formatCellValue(row.getCell(5));
//
//            ToolMaster tool = ToolMaster.builder()
//                    .toolMasterId(toolMasterId)
//                    .toolState(toolState)
//                    .toolCavity(cavity)
//                    .toolName(toolName)
//                    .build();
//
//            toolMasterRepository.save(tool);
//        }
//    }
//
//
//    public void exportToolExcel(String scenarioId, HttpServletResponse response) throws IOException {
//        List<ToolMaster> tools = toolMasterRepository.findAll();
//
//        Workbook workbook = new XSSFWorkbook();
//        Sheet sheet = workbook.createSheet("TOOL_MASTER");
//
//        String[] headers = {
//                "site_id", "tool_id", "tool_state", "tool_cavity", "scenario_id", "tool_name"
//        };
//
//        Row header = sheet.createRow(0);
//        for (int i = 0; i < headers.length; i++) {
//            header.createCell(i).setCellValue(headers[i]);
//        }
//
//        int rowIdx = 1;
//        for (ToolMaster tool : tools) {
//            Row row = sheet.createRow(rowIdx++);
//
//            ToolMasterId toolMasterId = tool.getToolMasterId();
//
//
//            String siteId = (toolMasterId.getSiteId() == null) ? "" : toolMasterId.getSiteId();
//            String toolId = (toolMasterId.getToolId() == null) ? "" : toolMasterId.getToolId();
//
//            String toolState = (tool.getToolState() == null) ? "" : tool.getToolState();
//            String toolName = (tool.getToolName() == null) ? "" : tool.getToolName();
//
//            Integer cavity = tool.getToolCavity();
//            int toolCavity = (cavity == null) ? 0 : cavity;
//
//            row.createCell(0).setCellValue(siteId);
//            row.createCell(1).setCellValue(toolId);
//            row.createCell(2).setCellValue(toolState);
//            row.createCell(3).setCellValue(toolCavity);
//            row.createCell(4).setCellValue(scenarioId);
//            row.createCell(5).setCellValue(toolName);
//        }
//
//        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
//        response.setHeader("Content-Disposition", "attachment; filename=tool_master_export.xlsx");
//
//        workbook.write(response.getOutputStream());
//        workbook.close();
//    }
//}
