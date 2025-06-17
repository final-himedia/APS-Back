package org.aps.engine.scenario.bop.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.aps.engine.scenario.bop.entity.Part;
import org.aps.engine.scenario.bop.entity.PartId;
import org.aps.engine.scenario.bop.repository.PartRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PartService {
    private final PartRepository partRepository;

    public void excelHandle(MultipartFile file, String scenarioId) throws IOException {
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);
        DataFormatter formatter = new DataFormatter();

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            String siteId =  formatter.formatCellValue(row.getCell(0));
            String partIdStr = formatter.formatCellValue(row.getCell(1));
            String partType =  formatter.formatCellValue(row.getCell(2));
            String routingId = formatter.formatCellValue(row.getCell(3));
            String partName =  formatter.formatCellValue(row.getCell(4));
            String minBatchStr =  formatter.formatCellValue(row.getCell(5));
            String maxBatchStr =  formatter.formatCellValue(row.getCell(6));
            String uom =  formatter.formatCellValue(row.getCell(7));


            int minBatchSize = minBatchStr.isEmpty() ? 0 : Integer.parseInt(minBatchStr);
            int maxBatchSize = maxBatchStr.isEmpty() ? 0 : Integer.parseInt(maxBatchStr);

            PartId partId = PartId.builder()
                    .siteId(siteId)
                    .partId(partIdStr)
                    .partType(partType)
                    .scenarioId(scenarioId)
                    .build();

            Part part = Part.builder()
                    .partId(partId)
                    .routingId(routingId)
                    .partName(partName)
                    .minBatchSize(minBatchSize)
                    .maxBatchSize(maxBatchSize)
                    .uom(uom)
                    .build();

            partRepository.save(part);
        }
        workbook.close();
    }

    public void exportPartExcel(String scenarioId,HttpServletResponse response) throws IOException {
        List<Part> parts = partRepository.findAll();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("PART");

        String[] headers = {
                "site_id", "part_id", "part_type", "routing_id",
                "part_name", "min_batch_size", "max_batch_size",
                "uom", "scenario_id"
        };

        Row header = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            header.createCell(i).setCellValue(headers[i]);
        }

        int rowIdx = 1;
        for (Part part : parts) {
            Row row = sheet.createRow(rowIdx++);
            PartId id = part.getPartId();

            row.createCell(0).setCellValue(id.getSiteId() == null ? "" : id.getSiteId());
            row.createCell(1).setCellValue(id.getPartId() == null ? "" : id.getPartId());
            row.createCell(2).setCellValue(id.getPartType() == null ? "" : id.getPartType());
            row.createCell(3).setCellValue(part.getRoutingId() == null ? "" : part.getRoutingId());
            row.createCell(4).setCellValue(part.getPartName() == null ? "" : part.getPartName());
            row.createCell(5).setCellValue(part.getMinBatchSize());
            row.createCell(6).setCellValue(part.getMaxBatchSize());
            row.createCell(7).setCellValue(part.getUom() == null ? "" : part.getUom());
            row.createCell(8).setCellValue(id.getScenarioId() == null ? "" : id.getScenarioId());
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=part_export.xlsx");

        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
