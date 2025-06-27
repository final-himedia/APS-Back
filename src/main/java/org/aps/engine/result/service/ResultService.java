package org.aps.engine.result.service;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.aps.engine.execution.result.WorkcenterPlan;
import org.aps.engine.execution.repository.WorkcenterPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ResultService {

    private final WorkcenterPlanRepository workcenterPlanRepository;

    public void excelHandle(String scenarioId, HttpServletResponse response) throws IOException {
        List<WorkcenterPlan> plans = workcenterPlanRepository.findAllByScenarioId(scenarioId);
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("PLAN_" + scenarioId);

        String[] headers = {
                "No", "Scenario ID", "Workcenter ID", "Workcenter Name",
                "Operation ID", "Operation Name", "Operation Type",
                "Start Time", "End Time", "Workcenter Group",
                "Tool ID", "Tool Name", "Routing ID"
        };

        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
        }

        int rowIdx = 1;
        for (WorkcenterPlan plan : plans) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(plan.getNo());
            row.createCell(1).setCellValue(plan.getScenarioId());
            row.createCell(2).setCellValue(plan.getWorkcenterId());
            row.createCell(3).setCellValue(plan.getWorkcenterName());
            row.createCell(4).setCellValue(plan.getOperationId());
            row.createCell(5).setCellValue(plan.getOperationName());
            row.createCell(6).setCellValue(plan.getOperationType());
            row.createCell(7).setCellValue(plan.getWorkcenterStartTime() != null ? plan.getWorkcenterStartTime().toString() : "");
            row.createCell(8).setCellValue(plan.getWorkcenterEndTime() != null ? plan.getWorkcenterEndTime().toString() : "");
            row.createCell(9).setCellValue(plan.getWorkcenterGroup());
            row.createCell(10).setCellValue(plan.getToolId());
            row.createCell(11).setCellValue(plan.getToolName());
            row.createCell(12).setCellValue(plan.getRoutingId());
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=workcenter_plan_" + scenarioId + ".xlsx");

        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
