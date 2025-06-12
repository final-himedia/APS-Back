package org.aps.engine.scenario.config.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.aps.engine.scenario.bop.entity.Operation;
import org.aps.engine.scenario.bop.entity.OperationId;
import org.aps.engine.scenario.config.entity.Priority;
import org.aps.engine.scenario.config.entity.PriorityId;
import org.aps.engine.scenario.config.repository.PriorityRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConfigService {

    private final PriorityRepository priorityRepository;

    public void excelHandle(MultipartFile file) throws IOException {
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);

            PriorityId priorityId = PriorityId.builder().
                    priorityId(row.getCell(0).getStringCellValue()).
                    factorId(row.getCell(1).getStringCellValue()).
                    scenarioId(row.getCell(6).getStringCellValue()).
                    build();

            Priority priority = Priority.builder().
                    priorityId(priorityId).
                    factorType(row.getCell(2).getStringCellValue()).
                    orderType(row.getCell(3).getStringCellValue()).
                    sequence((int) row.getCell(4).getNumericCellValue()).
                    description(row.getCell(5).getStringCellValue()).
                    build();

            priorityRepository.save(priority);
        }
    }

    public void exportPriorityExcel(HttpServletResponse response) throws IOException {
        List<Priority> priorities = priorityRepository.findAll();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("PRIORITY");

        String[] headers = {
                "priority_id", "factor_id", "factor_type", "order_type",
                "sequence", "description", "scenario_id"
        };

        // 헤더 생성
        Row header = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            header.createCell(i).setCellValue(headers[i]);
        }

        int rowIdx = 1;
        for (Priority priority : priorities) {
            Row row = sheet.createRow(rowIdx++);
            PriorityId id = priority.getPriorityId();

            row.createCell(0).setCellValue(id.getPriorityId());
            row.createCell(1).setCellValue(id.getFactorId());
            row.createCell(2).setCellValue(priority.getFactorType());
            row.createCell(3).setCellValue(priority.getOrderType());
            row.createCell(4).setCellValue(priority.getSequence());
            row.createCell(5).setCellValue(priority.getDescription());
            row.createCell(6).setCellValue(id.getScenarioId());
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=priority_export.xlsx");

        workbook.write(response.getOutputStream());
        workbook.close();
    }


}
