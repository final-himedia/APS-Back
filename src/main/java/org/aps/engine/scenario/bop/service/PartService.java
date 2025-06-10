package org.aps.engine.scenario.bop.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.aps.engine.scenario.bop.entity.Bom;
import org.aps.engine.scenario.bop.entity.BomId;
import org.aps.engine.scenario.bop.entity.Part;
import org.aps.engine.scenario.bop.entity.PartId;
import org.aps.engine.scenario.bop.repository.BomRepository;
import org.aps.engine.scenario.bop.repository.PartRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PartService {
    private final PartRepository partRepository;

    public void excelHandle(MultipartFile file) throws IOException {
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);

            PartId partId = PartId.builder().
                    siteId(row.getCell(0).getStringCellValue()).
                    partId(row.getCell(1).getStringCellValue()).
                    partType(row.getCell(2).getStringCellValue()).
                    build();

            Part part = Part.builder().
                    partId(partId).
                    routingId(row.getCell(3).getStringCellValue()).
                    partName(row.getCell(4).getStringCellValue()).
                    minBatchSize((int) row.getCell(5).getNumericCellValue()).
                    maxBatchSize((int) row.getCell(6).getNumericCellValue()).
                    uom(row.getCell(7).getStringCellValue()).
                    scenarioId(row.getCell(8).getStringCellValue()).
                    build();

        }
    }

    public void exportPartExcel(HttpServletResponse response) throws IOException {
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

            row.createCell(0).setCellValue(id.getSiteId());
            row.createCell(1).setCellValue(id.getPartId());
            row.createCell(2).setCellValue(id.getPartType());
            row.createCell(3).setCellValue(part.getRoutingId());
            row.createCell(4).setCellValue(part.getPartName());
            row.createCell(5).setCellValue(part.getMinBatchSize());
            row.createCell(6).setCellValue(part.getMaxBatchSize());
            row.createCell(7).setCellValue(part.getUom());
            row.createCell(8).setCellValue(part.getScenarioId());
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=part_export.xlsx");

        workbook.write(response.getOutputStream());
        workbook.close();
    }


}
