//package org.aps.engine.scenario.bop.service;
//
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.aps.engine.scenario.bop.entity.OperationRouting;
//import org.aps.engine.scenario.bop.entity.OperationRoutingId;
//import org.aps.engine.scenario.bop.repository.OperationRoutingRepository;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class OperationRoutingService {
//    private final OperationRoutingRepository operationRoutingRepository;
//
//    public void excelHandle(MultipartFile file, String scenarioId) throws IOException {
//        Workbook workbook = WorkbookFactory.create(file.getInputStream());
//        Sheet sheet = workbook.getSheetAt(0);
//        DataFormatter formatter = new DataFormatter();
//
//        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
//            Row row = sheet.getRow(i);
//            if (row == null) continue;
//
//            String siteId = (row.getCell(0) == null) ? "" : formatter.formatCellValue(row.getCell(0));
//            String routingId = (row.getCell(1) == null) ? "" : formatter.formatCellValue(row.getCell(1));
//            String operationId = (row.getCell(2) == null) ? "" : formatter.formatCellValue(row.getCell(2));
//            String operationSeqStr = (row.getCell(4) == null) ? "" : formatter.formatCellValue(row.getCell(4));
//            int operationSeq;
//
//            try {
//                operationSeq = operationSeqStr.isEmpty() ? 0 : Integer.parseInt(operationSeqStr);
//            } catch (NumberFormatException e) {
//                operationSeq = 0;
//            }
//
//            OperationRoutingId operationRoutingId = OperationRoutingId.builder()
//                    .siteId(siteId)
//                    .routingId(routingId)
//                    .operationId(operationId)
//                    .operationSeq(operationSeq)
//                    .scenarioId(scenarioId) // 전달받은 파라미터 사용
//                    .build();
//
//            String operationName = row.getCell(3) == null ? "" : formatter.formatCellValue(row.getCell(3));
//            String operationType = row.getCell(5) == null ? "" : formatter.formatCellValue(row.getCell(5));
//
//            OperationRouting operationRouting = OperationRouting.builder()
//                    .operationRoutingId(operationRoutingId)
//                    .operationName(operationName)
//                    .operationType(operationType)
//                    .build();
//
//            operationRoutingRepository.save(operationRouting);
//        }
//
//        workbook.close();
//    }
//
//
//    public void exportOperationRoutingExcel(String scenarioId, HttpServletResponse response) throws IOException {
//        List<OperationRouting> operationRoutings = operationRoutingRepository.findAll();
//
//        Workbook workbook = new XSSFWorkbook();
//        Sheet sheet = workbook.createSheet("OPERATIONROUTING");
//
//        String[] headers = {
//                "site_id", "routing_id", "operation_id", "operation_name",
//                "operation_seq", "operation_type", "scenario_id"
//        };
//
//        Row header = sheet.createRow(0);
//        for (int i = 0; i < headers.length; i++) {
//            header.createCell(i).setCellValue(headers[i]);
//        }
//
//        int rowIdx = 1;
//        for (OperationRouting operationRouting : operationRoutings) {
//            Row row = sheet.createRow(rowIdx++);
//            OperationRoutingId id = operationRouting.getOperationRoutingId();
//
//            row.createCell(0).setCellValue(id.getSiteId() == null ? "" : id.getSiteId());
//            row.createCell(1).setCellValue(id.getRoutingId() == null ? "" : id.getRoutingId());
//            row.createCell(2).setCellValue(id.getOperationId() == null ? "" : id.getOperationId());
//            row.createCell(3).setCellValue(operationRouting.getOperationName() == null ? "" : operationRouting.getOperationName());
//            row.createCell(4).setCellValue(id.getOperationSeq()); // int 기본형이라 null 체크 불필요
//            row.createCell(5).setCellValue(operationRouting.getOperationType() == null ? "" : operationRouting.getOperationType());
//            row.createCell(6).setCellValue(id.getScenarioId() == null ? "" : id.getScenarioId());
//        }
//
//        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
//        response.setHeader("Content-Disposition", "attachment; filename=operation_routing_export.xlsx");
//
//        workbook.write(response.getOutputStream());
//        workbook.close();
//    }
//}
