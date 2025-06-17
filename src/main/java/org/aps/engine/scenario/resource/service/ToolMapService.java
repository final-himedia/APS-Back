package org.aps.engine.scenario.resource.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.aps.engine.scenario.resource.entity.ToolMap;
import org.aps.engine.scenario.resource.entity.ToolMapId;
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

    public void excelHandle(MultipartFile file, String scenarioId) throws IOException {
        DataFormatter formatter = new DataFormatter();
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);

            ToolMapId toolMapId = ToolMapId.builder()
                    .scenarioId(scenarioId)
                    .partId(row.getCell(3) == null ? "" : formatter.formatCellValue(row.getCell(3)))
                    .toolId(row.getCell(4) == null ? "" : formatter.formatCellValue(row.getCell(4)))
                    .build();

            ToolMap tool = ToolMap.builder()
                    .siteId(row.getCell(0) == null ? "" : formatter.formatCellValue(row.getCell(0)))
                    .toolSize(row.getCell(1) == null ? "" : formatter.formatCellValue(row.getCell(1)))
                    .partName(row.getCell(5) == null ? "" : formatter.formatCellValue(row.getCell(5)))
                    .toolMapId(toolMapId)
                    .build();


            toolMapRepository.save(tool);
        }
    }

    public void exportToolMapExcel(String scenarioId, HttpServletResponse response) throws IOException {
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

            String siteId = tool.getSiteId() == null ? "" : tool.getSiteId();
            String toolSize = tool.getToolSize() == null ? "" : tool.getToolSize();

            ToolMapId toolMapId = tool.getToolMapId();
            String partId = (toolMapId == null || toolMapId.getPartId() == null) ? "" : toolMapId.getPartId();
            String toolId = (toolMapId == null || toolMapId.getToolId() == null) ? "" : toolMapId.getToolId();

            String partName = tool.getPartName() == null ? "" : tool.getPartName();

            row.createCell(0).setCellValue(siteId);
            row.createCell(1).setCellValue(toolSize);
            row.createCell(2).setCellValue(scenarioId);
            row.createCell(3).setCellValue(partId);
            row.createCell(4).setCellValue(toolId);
            row.createCell(5).setCellValue(partName);
        }

        // 응답 헤더 설정
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=tool_map_export.xlsx");

        workbook.write(response.getOutputStream());
        workbook.close();
    }

}
