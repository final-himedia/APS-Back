package org.aps.engine.scenario.bop.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.aps.engine.scenario.bop.entity.Operation;
import org.aps.engine.scenario.bop.entity.OperationId;
import org.aps.engine.scenario.bop.repository.OperationRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OperationService {
    private final OperationRepository operationRepository;

    public void excelHandle(MultipartFile file, String scenarioId) throws IOException {
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);
        DataFormatter formatter = new DataFormatter();

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            String siteId = (row.getCell(0) == null) ? "" : formatter.formatCellValue(row.getCell(0));
            String operationIdStr = (row.getCell(1) == null) ? "" : formatter.formatCellValue(row.getCell(1));

            OperationId operationId = OperationId.builder()
                    .siteId(siteId)
                    .operationId(operationIdStr)
                    .scenarioId(scenarioId)
                    .build();

            Operation operation = Operation.builder()
                    .operationId(operationId)
                    .operationName(row.getCell(2) == null ? "" : formatter.formatCellValue(row.getCell(2)))
                    .runTime(row.getCell(3) == null ? "" : formatter.formatCellValue(row.getCell(3)))
                    .yield(row.getCell(4) == null ? "" : formatter.formatCellValue(row.getCell(4)))
                    .waitTime(row.getCell(5) == null ? "" : formatter.formatCellValue(row.getCell(5)))
                    .transferTime(row.getCell(6) == null ? "" : formatter.formatCellValue(row.getCell(6)))
                    .runTimeUom(row.getCell(7) == null ? "" : formatter.formatCellValue(row.getCell(7)))
                    .operationSeq(row.getCell(8) == null ? "" : formatter.formatCellValue(row.getCell(8)))
                    .operationType(row.getCell(9) == null ? "" : formatter.formatCellValue(row.getCell(9)))
                    .waitTimeUom(row.getCell(10) == null ? "" : formatter.formatCellValue(row.getCell(10)))
                    .transferTimeUom(row.getCell(11) == null ? "" : formatter.formatCellValue(row.getCell(11)))
                    .sourcingType(row.getCell(13) == null ? "" : formatter.formatCellValue(row.getCell(13)))
                    .build();


            operationRepository.save(operation);
        }
        workbook.close();
    }

    public void exportOperationExcel(String scenarioId,HttpServletResponse response) throws IOException {
        List<Operation> operations = operationRepository.findAll();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("OPERATION");

        String[] headers = {
                "site_id", "operation_id", "operation_name", "run_time",
                "yield", "wait_time", "transfer_time", "run_time_uom",
                "operation_seq", "operation_type", "wait_time_uom", "transfer_time_uom", "scenario_id"
        };

        Row header = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            header.createCell(i).setCellValue(headers[i]);
        }

        int rowIdx = 1;
        for (Operation operation : operations) {
            Row row = sheet.createRow(rowIdx++);
            OperationId id = operation.getOperationId();

            row.createCell(0).setCellValue(id.getSiteId() == null ? "" : id.getSiteId());
            row.createCell(1).setCellValue(id.getOperationId() == null ? "" : id.getOperationId());
            row.createCell(2).setCellValue(operation.getOperationName() == null ? "" : operation.getOperationName());
            row.createCell(3).setCellValue(operation.getRunTime() == null ? "" : operation.getRunTime());
            row.createCell(4).setCellValue(operation.getYield() == null ? "" : operation.getYield());
            row.createCell(5).setCellValue(operation.getWaitTime() == null ? "" : operation.getWaitTime());
            row.createCell(6).setCellValue(operation.getTransferTime() == null ? "" : operation.getTransferTime());
            row.createCell(7).setCellValue(operation.getRunTimeUom() == null ? "" : operation.getRunTimeUom());
            row.createCell(8).setCellValue(operation.getOperationSeq() == null ? "" : operation.getOperationSeq());
            row.createCell(9).setCellValue(operation.getOperationType() == null ? "" : operation.getOperationType());
            row.createCell(10).setCellValue(operation.getWaitTimeUom() == null ? "" : operation.getWaitTimeUom());
            row.createCell(11).setCellValue(operation.getTransferTimeUom() == null ? "" : operation.getTransferTimeUom());
            row.createCell(12).setCellValue(scenarioId);
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=operation_export.xlsx");

        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
