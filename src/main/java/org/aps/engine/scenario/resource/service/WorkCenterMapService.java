package org.aps.engine.scenario.resource.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.aps.engine.scenario.bop.entity.Operation;
import org.aps.engine.scenario.bop.entity.OperationId;
import org.aps.engine.scenario.resource.entity.WorkCenterMap;
import org.aps.engine.scenario.resource.repository.WorkCenterMapRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkCenterMapService {
    private final WorkCenterMapRepository workCenterMapRepository;

    public void excelHandle(MultipartFile file) throws IOException {
        DataFormatter formatter = new DataFormatter();
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);

            WorkCenterMap workCenterMap = WorkCenterMap.builder()
                    .siteId(formatter.formatCellValue(row.getCell(0)))
                    .routingGroup(formatter.formatCellValue(row.getCell(1)))
                    .partId(formatter.formatCellValue(row.getCell(2)))
                    .operationId(formatter.formatCellValue(row.getCell(3)))
                    .routingGroup(formatter.formatCellValue(row.getCell(4)))  // routingGroup이 두번인데 맞나요?
                    .routingVersion(formatter.formatCellValue(row.getCell(5)))
                    .workcenterId(formatter.formatCellValue(row.getCell(6)))
                    .tactTime(formatter.formatCellValue(row.getCell(7)))
                    .tactTimeUom(formatter.formatCellValue(row.getCell(8)))
                    .procTime(formatter.formatCellValue(row.getCell(9)))
                    .procTimeUom(formatter.formatCellValue(row.getCell(10)))
                    .scenarioId(formatter.formatCellValue(row.getCell(11)))
                    .build();

            workCenterMapRepository.save(workCenterMap);
        }
    }


    public void exportWorkCenterMapExcel(HttpServletResponse response) throws IOException {
        List<WorkCenterMap> workCenterMaps = workCenterMapRepository.findAll();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("WORK_CENTER_MAP");

        String[] headers = {
                "site_id", "routing_group", "part_id", "operation_id", "routing_group2",
                "routing_version", "workcenter_id", "tact_time", "tact_time_uom",
                "proc_time", "proc_time_uom", "scenario_id"
        };

        Row header = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            header.createCell(i).setCellValue(headers[i]);
        }

        int rowIdx = 1;
        for (WorkCenterMap map : workCenterMaps) {
            Row row = sheet.createRow(rowIdx++);

            row.createCell(0).setCellValue(map.getSiteId());
            row.createCell(1).setCellValue(map.getRoutingGroup());  // 첫 번째 routingGroup
            row.createCell(2).setCellValue(map.getPartId());
            row.createCell(3).setCellValue(map.getOperationId());
            row.createCell(4).setCellValue(map.getRoutingGroup());  // 두 번째 routingGroup (동일 값 저장)
            row.createCell(5).setCellValue(map.getRoutingVersion());
            row.createCell(6).setCellValue(map.getWorkcenterId());
            row.createCell(7).setCellValue(map.getTactTime());
            row.createCell(8).setCellValue(map.getTactTimeUom());
            row.createCell(9).setCellValue(map.getProcTime());
            row.createCell(10).setCellValue(map.getProcTimeUom());
            row.createCell(11).setCellValue(map.getScenarioId());
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=work_center_map_export.xlsx");

        workbook.write(response.getOutputStream());
        workbook.close();
    }

}
