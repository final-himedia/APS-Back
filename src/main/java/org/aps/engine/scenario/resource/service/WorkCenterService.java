package org.aps.engine.scenario.resource.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.aps.engine.scenario.resource.entity.WorkCenter;
import org.aps.engine.scenario.resource.repository.WorkCenterRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkCenterService {
    private final WorkCenterRepository workCenterRepository;

    public void excelHandle(MultipartFile file) throws IOException {
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);

            WorkCenter workCenter = WorkCenter.builder()
                    .siteId(row.getCell(0).getStringCellValue())
                    .workcenterId(row.getCell(1).getStringCellValue())
                    .workcenterName(row.getCell(2).getStringCellValue())
                    .workcenterGroup(row.getCell(3).getStringCellValue())
                    .workcenterType(row.getCell(4).getStringCellValue())
                    .priorityId(row.getCell(5).getStringCellValue())
                    .dispatcherType(row.getCell(6).getStringCellValue())
                    .workcenterState(row.getCell(7).getStringCellValue())
                    .automation(row.getCell(8).getStringCellValue())
                    .scenarioId(row.getCell(9).getStringCellValue())
                    .build();

            workCenterRepository.save(workCenter);
        }
    }

    public void exportWorkCenterExcel(HttpServletResponse response) throws IOException {
        List<WorkCenter> workCenters = workCenterRepository.findAll();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("WORK_CENTER");

        String[] headers = {
                "site_id", "workcenter_id", "workcenter_name", "workcenter_group",
                "workcenter_type", "priority_id", "dispatcher_type",
                "workcenter_state", "automation", "scenario_id"
        };

        Row header = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            header.createCell(i).setCellValue(headers[i]);
        }

        int rowIdx = 1;
        for (WorkCenter workCenter : workCenters) {
            Row row = sheet.createRow(rowIdx++);

            row.createCell(0).setCellValue(workCenter.getSiteId());
            row.createCell(1).setCellValue(workCenter.getWorkcenterId());
            row.createCell(2).setCellValue(workCenter.getWorkcenterName());
            row.createCell(3).setCellValue(workCenter.getWorkcenterGroup());
            row.createCell(4).setCellValue(workCenter.getWorkcenterType());
            row.createCell(5).setCellValue(workCenter.getPriorityId());
            row.createCell(6).setCellValue(workCenter.getDispatcherType());
            row.createCell(7).setCellValue(workCenter.getWorkcenterState());
            row.createCell(8).setCellValue(workCenter.getAutomation());
            row.createCell(9).setCellValue(workCenter.getScenarioId());
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=workcenter_export.xlsx");

        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
