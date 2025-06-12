package org.aps.engine.scenario.resource.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.aps.engine.scenario.resource.entity.ToolMap;
import org.aps.engine.scenario.resource.entity.ToolMaster;
import org.aps.engine.scenario.resource.repository.ToolMapRepository;
import org.aps.engine.scenario.resource.repository.ToolMasterRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ToolMapService {
    private final ToolMapRepository toolMapRepository;

    public void excelHandle(MultipartFile file) throws IOException {
        DataFormatter formatter = new DataFormatter();
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);

            ToolMap tool = ToolMap.builder()
                    .siteId(formatter.formatCellValue(row.getCell(0)))
                    .toolSize(formatter.formatCellValue(row.getCell(1)))
                    .scenarioId(formatter.formatCellValue(row.getCell(2)))
                    .partId(formatter.formatCellValue(row.getCell(3)))
                    .toolId(formatter.formatCellValue(row.getCell(4)))
                    .partName(formatter.formatCellValue(row.getCell(5)))
                    .build();

            toolMapRepository.save(tool);
        }
    }


    public void exportToolExcel(HttpServletResponse response) throws IOException {
        List<ToolMap> tools = toolMapRepository.findAll();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("TOOL_MAP");

        String[] headers = {
                "site_id", "tool_size", "scenario_id", "part_id", "tool_id", "part_name"
        };

        // 헤더 작성
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
        }

        // 데이터 작성
        int rowIdx = 1;
        for (ToolMap tool : tools) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(tool.getSiteId());
            row.createCell(1).setCellValue(tool.getToolSize());
            row.createCell(2).setCellValue(tool.getScenarioId());
            row.createCell(3).setCellValue(tool.getPartId());
            row.createCell(4).setCellValue(tool.getToolId());
            row.createCell(5).setCellValue(tool.getPartName());
        }

        // 응답 헤더 설정
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=tool_map_export.xlsx");

        workbook.write(response.getOutputStream());
        workbook.close();
    }

}
