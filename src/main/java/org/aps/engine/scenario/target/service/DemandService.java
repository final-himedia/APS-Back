package org.aps.engine.scenario.target.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.aps.engine.scenario.target.entity.Demand;
import org.aps.engine.scenario.target.entity.DemandId;
import org.aps.engine.scenario.target.repository.DemandRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DemandService {
    private final DemandRepository demandRepository;

    public void excelHandle(MultipartFile file, String scenarioId) throws IOException {
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);
        DataFormatter formatter = new DataFormatter();

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);

            DemandId demandId = DemandId.builder()
                    .demandId(formatter.formatCellValue(row.getCell(0)))
                    .siteId(formatter.formatCellValue(row.getCell(1)))
                    .partId(formatter.formatCellValue(row.getCell(2)))
                    .scenarioId(scenarioId)
                    .build();

            Demand demand = Demand.builder()
                    .demandId(demandId)
                    .partName(formatter.formatCellValue(row.getCell(3)))
                    .customerId(formatter.formatCellValue(row.getCell(4)))
                    .dueDate(LocalDateTime.parse(formatter.formatCellValue(row.getCell(5))))
                    .demandQty(Double.valueOf(formatter.formatCellValue(row.getCell(6))))
                    .priority(Float.valueOf(formatter.formatCellValue(row.getCell(7))))
                    .uom(formatter.formatCellValue(row.getCell(8)))
                    .orderType(formatter.formatCellValue(row.getCell(9)))
                    .orderTypeName(formatter.formatCellValue(row.getCell(10)))
                    .exceptYn(formatter.formatCellValue(row.getCell(11)))
                    .headerCreationDate(LocalDateTime.parse(formatter.formatCellValue(row.getCell(12))))
                    .hasOverActQty("true".equalsIgnoreCase(formatter.formatCellValue(row.getCell(13))))
                    .build();

            demandRepository.save(demand);
        }
    }

    public void exportDemandExcel(String scenarioId, HttpServletResponse response) throws IOException {
        List<Demand> demands = demandRepository.findAll();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("DEMAND");

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("demand_id");
        header.createCell(1).setCellValue("site_id");
        header.createCell(2).setCellValue("part_id");
        header.createCell(3).setCellValue("part_name");
        header.createCell(4).setCellValue("customer_id");
        header.createCell(5).setCellValue("due_date");
        header.createCell(6).setCellValue("demand_qty");
        header.createCell(7).setCellValue("priority");
        header.createCell(8).setCellValue("uom");
        header.createCell(9).setCellValue("order_type");
        header.createCell(10).setCellValue("order_type_name");
        header.createCell(11).setCellValue("except_yn");
        header.createCell(12).setCellValue("header_creation_date");
        header.createCell(13).setCellValue("has_over_act_qty");
        header.createCell(14).setCellValue("scenario_id");

        int rowIdx = 1;
        for (Demand demand : demands) {
            Row row = sheet.createRow(rowIdx++);
            DemandId id = demand.getDemandId();

            row.createCell(0).setCellValue(id.getDemandId() == null ? "" : id.getDemandId());
            row.createCell(1).setCellValue(id.getSiteId() == null ? "" : id.getSiteId());
            row.createCell(2).setCellValue(id.getPartId() == null ? "" : id.getPartId());
            row.createCell(3).setCellValue(demand.getPartName() == null ? "" : demand.getPartName());
            row.createCell(4).setCellValue(demand.getCustomerId() == null ? "" : demand.getCustomerId());
            row.createCell(5).setCellValue(demand.getDueDate() == null ? "" : demand.getDueDate().toString());
            row.createCell(6).setCellValue(demand.getDemandQty() == null ? "" : demand.getDemandQty().toString());
            row.createCell(7).setCellValue(demand.getPriority() == null ? "" : demand.getPriority().toString());
            row.createCell(8).setCellValue(demand.getUom() == null ? "" : demand.getUom());
            row.createCell(9).setCellValue(demand.getOrderType() == null ? "" : demand.getOrderType());
            row.createCell(10).setCellValue(demand.getOrderTypeName() == null ? "" : demand.getOrderTypeName());
            row.createCell(11).setCellValue(demand.getExceptYn() == null ? "" : demand.getExceptYn());
            row.createCell(12).setCellValue(demand.getHeaderCreationDate() == null ? "" : demand.getHeaderCreationDate().toString());
            row.createCell(13).setCellValue(demand.getHasOverActQty() == null ? "" : demand.getHasOverActQty().toString());
            row.createCell(14).setCellValue(scenarioId);

        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=demand_export.xlsx");

        workbook.write(response.getOutputStream());
        workbook.close();
    }

}
