package org.aps.engine.scenario.bop.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.aps.engine.scenario.bop.entity.Demand;
import org.aps.engine.scenario.bop.entity.DemandId;
import org.aps.engine.scenario.bop.entity.OperationRouting;
import org.aps.engine.scenario.bop.entity.OperationRoutingId;
import org.aps.engine.scenario.bop.repository.DemandRepository;
import org.aps.engine.scenario.bop.repository.OperationRoutingRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OperationRoutingService {
    private final OperationRoutingRepository operationRoutingRepository;

    public void excelHandle(MultipartFile file) throws IOException {
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);

            OperationRoutingId operationRoutingId = OperationRoutingId.builder().
                    siteId(row.getCell(0).getStringCellValue()).
                    routingId(row.getCell(1).getStringCellValue()).
                    operationId(row.getCell(2).getStringCellValue()).
                    operationSeq((int) row.getCell(4).getNumericCellValue()).
                    build();

            OperationRouting operationRouting = OperationRouting.builder().
                    operationRoutingId(operationRoutingId).
                    operationName(row.getCell(3).getStringCellValue()).
                    operationType(row.getCell(5).getStringCellValue()).
                    scenarioId(row.getCell(6).getStringCellValue()).
                    build();


            operationRoutingRepository.save(operationRouting);
        }
    }
    public void exportOperationRoutingExcel(HttpServletResponse response) throws IOException {
        List<OperationRouting> operationRoutings = operationRoutingRepository.findAll();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("OperationRouting");

        String[] headers = {
                "site_id", "routing_id", "operation_id", "operation_name",
                "operation_seq", "operation_type", "scenario_id"
        };

        Row header = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            header.createCell(i).setCellValue(headers[i]);
        }

        int rowIdx = 1;
        for (OperationRouting operationRouting : operationRoutings) {
            Row row = sheet.createRow(rowIdx++);
            OperationRoutingId id = operationRouting.getOperationRoutingId();

            row.createCell(0).setCellValue(id.getSiteId());
            row.createCell(1).setCellValue(id.getRoutingId());
            row.createCell(2).setCellValue(id.getOperationId());
            row.createCell(3).setCellValue(operationRouting.getOperationName());
            row.createCell(4).setCellValue(id.getOperationSeq());
            row.createCell(5).setCellValue(operationRouting.getOperationType());
            row.createCell(6).setCellValue(operationRouting.getScenarioId());
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=operation_routing_export.xlsx");

        workbook.write(response.getOutputStream());
        workbook.close();
    }

}
