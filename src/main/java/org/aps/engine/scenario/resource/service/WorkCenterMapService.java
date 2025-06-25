package org.aps.engine.scenario.resource.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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

    public void excelHandle(MultipartFile file, String scenarioId) throws IOException {
        DataFormatter formatter = new DataFormatter();
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);



            WorkCenterMap workCenterMap = WorkCenterMap.builder()
                    .routingId(row.getCell(0) == null ? "" : formatter.formatCellValue(row.getCell(1)))
                    .partId(row.getCell(1) == null ? "" : formatter.formatCellValue(row.getCell(2)))
                    .routingGroup(row.getCell(2) == null ? "" : formatter.formatCellValue(row.getCell(4)))
                    .routingVersion(row.getCell(3) == null ? "" : formatter.formatCellValue(row.getCell(5)))
                    .tactTime(row.getCell(4) == null ? "" : formatter.formatCellValue(row.getCell(7)))
                    .tactTimeUom(row.getCell(5) == null ? "" : formatter.formatCellValue(row.getCell(8)))
                    .procTime(row.getCell(6) == null ? "" : formatter.formatCellValue(row.getCell(9)))
                    .procTimeUom(row.getCell(7) == null ? "" : formatter.formatCellValue(row.getCell(10)))
                    .build();

            workCenterMapRepository.save(workCenterMap);
        }
    }


    public void exportWorkCenterMapExcel(String scenarioId, HttpServletResponse response) throws IOException {
        List<WorkCenterMap> workCenterMaps = workCenterMapRepository.findAll();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("WORK_CENTER_MAP");

        String[] headers = {
                "site_id", "routing_id", "part_id", "operation_id", "routing_group", "routing_version",
                "workcenter_id", "tact_time", "tact_time_uom", "proc_time", "proc_time_uom", "scenario_id"
        };

        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
        }

        int rowIdx = 1;
        for (WorkCenterMap map : workCenterMaps) {
            Row row = sheet.createRow(rowIdx++);




            row.createCell(1).setCellValue(map.getRoutingId() == null ? "" : map.getRoutingId());
            row.createCell(2).setCellValue(map.getPartId() == null ? "" : map.getPartId());
            row.createCell(3).setCellValue(map.getRoutingGroup() == null ? "" : map.getRoutingGroup());
            row.createCell(4).setCellValue(map.getRoutingVersion() == null ? "" : map.getRoutingVersion());
            row.createCell(5).setCellValue(map.getTactTime() == null ? "" : map.getTactTime());
            row.createCell(6).setCellValue(map.getTactTimeUom() == null ? "" : map.getTactTimeUom());
            row.createCell(7).setCellValue(map.getProcTime() == null ? "" : map.getProcTime());
            row.createCell(8).setCellValue(map.getProcTimeUom() == null ? "" : map.getProcTimeUom());
            row.createCell(9).setCellValue(scenarioId);
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=work_center_map_export.xlsx");

        workbook.write(response.getOutputStream());
        workbook.close();
    }

}
