package org.aps.engine.scenario.resource.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.aps.engine.scenario.bop.entity.Operation;
import org.aps.engine.scenario.bop.entity.OperationId;
import org.aps.engine.scenario.resource.entity.ToolMaster;
import org.aps.engine.scenario.resource.entity.ToolMasterId;
import org.aps.engine.scenario.resource.repository.ToolMasterRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ToolMasterService {
    private final ToolMasterRepository toolMasterRepository;

    public void excelHandle(MultipartFile file) throws IOException {
        DataFormatter formatter = new DataFormatter();
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);

            ToolMasterId toolMasterId = ToolMasterId.builder()
                    .siteId(formatter.formatCellValue(row.getCell(0)))
                    .toolId(formatter.formatCellValue(row.getCell(1)))
                    .scenarioId(formatter.formatCellValue(row.getCell(4)))
                    .build();

            ToolMaster tool = ToolMaster.builder()


                    .toolState(formatter.formatCellValue(row.getCell(2)))
                    .toolCavity(Integer.parseInt(formatter.formatCellValue(row.getCell(3))))
                    .toolName(formatter.formatCellValue(row.getCell(5)))
                    .build();

            toolMasterRepository.save(tool);
        }
    }


    public void exportToolExcel(HttpServletResponse response) throws IOException {
        List<ToolMaster> tools = toolMasterRepository.findAll();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("TOOL_MASTER");

        String[] headers = {
                "site_id", "tool_id", "tool_state", "tool_cavity", "scenario_id", "tool_name"
        };

        // 헤더 생성
        Row header = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            header.createCell(i).setCellValue(headers[i]);
        }

        // 데이터 채우기
        int rowIdx = 1;
        for (ToolMaster tool : tools) {
            Row row = sheet.createRow(rowIdx++);

            ToolMasterId toolMasterId = tool.getToolMasterId();

            row.createCell(0).setCellValue(toolMasterId.getSiteId());
            row.createCell(1).setCellValue(toolMasterId.getToolId());
            row.createCell(2).setCellValue(tool.getToolState());
            row.createCell(3).setCellValue(tool.getToolCavity());
            row.createCell(4).setCellValue(toolMasterId.getScenarioId());
            row.createCell(5).setCellValue(tool.getToolName());
        }

        // HTTP 응답 설정
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=tool_master_export.xlsx");

        workbook.write(response.getOutputStream());
        workbook.close();
    }


}
