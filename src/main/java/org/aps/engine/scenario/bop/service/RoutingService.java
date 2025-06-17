package org.aps.engine.scenario.bop.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.aps.engine.scenario.bop.entity.Routing;
import org.aps.engine.scenario.bop.entity.RoutingId;
import org.aps.engine.scenario.bop.repository.RoutingRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoutingService {
    private final RoutingRepository routingRepository;

    public void routingExcelHandle(MultipartFile file, String scenarioId) throws IOException {
        DataFormatter formatter = new DataFormatter();
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            String siteId = (row.getCell(0) == null) ? "" : formatter.formatCellValue(row.getCell(0));
            String routingIdStr = (row.getCell(1) == null) ? "" : formatter.formatCellValue(row.getCell(1));
            String routingName = (row.getCell(2) == null) ? "" : formatter.formatCellValue(row.getCell(2));
            String routingType = (row.getCell(3) == null) ? "" : formatter.formatCellValue(row.getCell(3));

            RoutingId routingId = RoutingId.builder()
                    .siteId(siteId)
                    .routingId(routingIdStr)
                    .scenarioId(scenarioId)
                    .build();

            Routing routing = Routing.builder()
                    .routingId(routingId)
                    .routingName(routingName)
                    .routingType(routingType)
                    .build();

            routingRepository.save(routing);
        }

        workbook.close();
    }

    public void exportRoutingExcel(String scenarioId,HttpServletResponse response) throws IOException {
        List<Routing> routings = routingRepository.findAll();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("ROUTING");

        String[] headers = { "site_id", "routing_id", "routing_name", "routing_type", "scenario_id" };
        Row header = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            header.createCell(i).setCellValue(headers[i]);
        }

        int rowIdx = 1;
        for (Routing routing : routings) {
            Row row = sheet.createRow(rowIdx++);
            RoutingId id = routing.getRoutingId();

            row.createCell(0).setCellValue(id.getSiteId() == null ? "" : id.getSiteId());
            row.createCell(1).setCellValue(id.getRoutingId() == null ? "" : id.getRoutingId());
            row.createCell(2).setCellValue(routing.getRoutingName() == null ? "" : routing.getRoutingName());
            row.createCell(3).setCellValue(routing.getRoutingType() == null ? "" : routing.getRoutingType());
            row.createCell(4).setCellValue(id.getScenarioId() == null ? "" : id.getScenarioId());
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=routing_export.xlsx");

        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
