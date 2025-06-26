package org.aps.engine.scenario.resource.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.aps.engine.scenario.resource.entity.ToolMap;
import org.aps.engine.scenario.resource.repository.ToolMapRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ToolMapService {
    private final ToolMapRepository toolMapRepository;

    public void excelHandle(MultipartFile file, String scenarioId) throws IOException {
        DataFormatter formatter = new DataFormatter();
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            ToolMap tool = ToolMap.builder()
                    .siteId(getCellValue(row, 0, formatter))
                    .toolSize(getCellValue(row, 1, formatter))
                    .scenarioId(scenarioId) // 고정 파라미터로 전달됨
                    .partId(getCellValue(row, 3, formatter))
                    .toolId(getCellValue(row, 4, formatter))
                    .partName(getCellValue(row, 5, formatter))
                    .operationId(getCellValue(row, 6, formatter))
                    .workcenterId(getCellValue(row, 7, formatter))
                    .build();

            toolMapRepository.save(tool);
        }
        workbook.close();
    }

    public void exportToolMapExcel(String scenarioId, HttpServletResponse response) throws IOException {
        List<ToolMap> tools = toolMapRepository.findAll();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("TOOL_MAP");

        String[] headers = {
                "site_id", "tool_size", "scenario_id", "part_id",
                "tool_id", "part_name", "operation_id", "workcenter_id"
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

            row.createCell(0).setCellValue(safe(tool.getSiteId()));
            row.createCell(1).setCellValue(safe(tool.getToolSize()));
            row.createCell(2).setCellValue(safe(tool.getScenarioId()));
            row.createCell(3).setCellValue(safe(tool.getPartId()));
            row.createCell(4).setCellValue(safe(tool.getToolId()));
            row.createCell(5).setCellValue(safe(tool.getPartName()));
            row.createCell(6).setCellValue(safe(tool.getOperationId()));
            row.createCell(7).setCellValue(safe(tool.getWorkcenterId()));
        }

        // 응답 헤더 설정
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=tool_map_export.xlsx");

        workbook.write(response.getOutputStream());
        workbook.close();
    }

    private String getCellValue(Row row, int cellIndex, DataFormatter formatter) {
        Cell cell = row.getCell(cellIndex);
        return cell == null ? "" : formatter.formatCellValue(cell);
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }
}
