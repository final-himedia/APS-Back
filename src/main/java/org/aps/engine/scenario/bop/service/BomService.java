package org.aps.engine.scenario.bop.service;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.aps.engine.scenario.bop.entity.Bom;
import org.aps.engine.scenario.bop.entity.BomId;
import org.aps.engine.scenario.bop.repository.BomRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BomService {
    private final BomRepository bomRepository;

    public void excelHandle(MultipartFile file) throws IOException {
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);
        DataFormatter formatter = new DataFormatter();

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);

            BomId bomId = BomId.builder()
                    .toSiteId(formatter.formatCellValue(row.getCell(0)))
                    .toPartId(formatter.formatCellValue(row.getCell(1)))
                    .fromSiteId(formatter.formatCellValue(row.getCell(6)))
                    .fromPartId(formatter.formatCellValue(row.getCell(7)))
                    .zseq(formatter.formatCellValue(row.getCell(16)))
                    .scenarioId(formatter.formatCellValue(row.getCell(17)))
                    .build();

            Bom bom = Bom.builder()
                    .bomId(bomId)
                    .operationId(formatter.formatCellValue(row.getCell(2)))
                    .bomCategory(formatter.formatCellValue(row.getCell(3)))
                    .outQty(formatter.formatCellValue(row.getCell(4)))
                    .outUom(formatter.formatCellValue(row.getCell(5)))
                    .inQty(formatter.formatCellValue(row.getCell(8)))
                    .inUom(formatter.formatCellValue(row.getCell(9)))
                    .createDatetime(formatter.formatCellValue(row.getCell(10)))
                    .effStartDate(formatter.formatCellValue(row.getCell(11)))
                    .createBy(formatter.formatCellValue(row.getCell(12)))
                    .toPartName(formatter.formatCellValue(row.getCell(13)))
                    .fromPartName(formatter.formatCellValue(row.getCell(14)))
                    .bomText(formatter.formatCellValue(row.getCell(15)))
                    .bomVersion(formatter.formatCellValue(row.getCell(18)))
                    .fromPartLevel(formatter.formatCellValue(row.getCell(19)))
                    .toPartLevel(formatter.formatCellValue(row.getCell(20)))
                    .build();

            bomRepository.save(bom);
        }
    }


    public void exportBomExcel(HttpServletResponse response) throws IOException {
        List<Bom> boms = bomRepository.findAll();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("BOM");

        // 21개 컬럼 헤더
        String[] headers = {
                "to_site_id", "to_part_id", "operation_id", "bom_category", "out_qty", "out_uom",
                "from_site_id", "from_part_id", "in_qty", "in_uom",
                "create_datetime", "eff_start_date", "create_by",
                "to_part_name", "from_part_name", "bom_text", "zseq", "scenario_id",
                "bom_version", "from_part_level", "to_part_level"
        };

        Row header = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            header.createCell(i).setCellValue(headers[i]);
        }

        int rowIdx = 1;
        for (Bom bom : boms) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(bom.getBomId().getToSiteId());
            row.createCell(1).setCellValue(bom.getBomId().getToPartId());
            row.createCell(2).setCellValue(bom.getOperationId());
            row.createCell(3).setCellValue(bom.getBomCategory());
            row.createCell(4).setCellValue(bom.getOutQty());
            row.createCell(5).setCellValue(bom.getOutUom());
            row.createCell(6).setCellValue(bom.getBomId().getFromSiteId());
            row.createCell(7).setCellValue(bom.getBomId().getFromPartId());
            row.createCell(8).setCellValue(bom.getInQty());
            row.createCell(9).setCellValue(bom.getInUom());
            row.createCell(10).setCellValue(bom.getCreateDatetime());
            row.createCell(11).setCellValue(bom.getEffStartDate());
            row.createCell(12).setCellValue(bom.getCreateBy());
            row.createCell(13).setCellValue(bom.getToPartName());
            row.createCell(14).setCellValue(bom.getFromPartName());
            row.createCell(15).setCellValue(bom.getBomText());
            row.createCell(16).setCellValue(bom.getBomId().getZseq());
            row.createCell(17).setCellValue(bom.getBomId().getScenarioId());
            row.createCell(18).setCellValue(bom.getBomVersion());
            row.createCell(19).setCellValue(bom.getFromPartLevel());
            row.createCell(20).setCellValue(bom.getToPartLevel());
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=bom_export.xlsx");

        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
