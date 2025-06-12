package org.aps.engine.scenario.bop.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.aps.engine.scenario.bop.entity.Bom;
import org.aps.engine.scenario.bop.entity.BomId;
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

    public void routingExcelHandle(MultipartFile file) throws IOException {
        DataFormatter formatter = new DataFormatter();
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            RoutingId routingId = RoutingId.builder()
                    .siteId(formatter.formatCellValue(row.getCell(0)))
                    .routingId(formatter.formatCellValue(row.getCell(1)))
                    .build();

            Routing routing = Routing.builder()
                    .routingId(routingId)
                    .routingName(formatter.formatCellValue(row.getCell(2)))
                    .routingType(formatter.formatCellValue(row.getCell(3)))
                    .scenarioId(formatter.formatCellValue(row.getCell(4)))
                    .build();

            routingRepository.save(routing);
        }
    }

    public void exportRoutingExcel(HttpServletResponse response) throws IOException {
        List<Routing> routings = routingRepository.findAll();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("ROUTING");

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("site_id");
        header.createCell(1).setCellValue("routing_id");
        header.createCell(2).setCellValue("routing_name");
        header.createCell(3).setCellValue("routing_type");
        header.createCell(4).setCellValue("scenario_id");

        int rowIdx = 1;
        for (Routing routing : routings) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(routing.getRoutingId().getSiteId());
            row.createCell(1).setCellValue(routing.getRoutingId().getRoutingId());
            row.createCell(2).setCellValue(routing.getRoutingName());
            row.createCell(3).setCellValue(routing.getRoutingType());
            row.createCell(4).setCellValue(routing.getScenarioId());

        }

        // 응답 설정
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=bom_export.xlsx");

        workbook.write(response.getOutputStream());
        workbook.close();
    }
}

