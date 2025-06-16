package org.aps.engine.scenario.bop.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.aps.engine.scenario.bop.entity.Bom;
import org.aps.engine.scenario.bop.entity.BomId;
import org.aps.engine.scenario.bop.repository.BomRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
            if (row == null) continue;

            BomId bomId = BomId.builder()
                    .toSiteId(row.getCell(0) == null ? "" : formatter.formatCellValue(row.getCell(0)))
                    .toPartId(row.getCell(1) == null ? "" : formatter.formatCellValue(row.getCell(1)))
                    .fromSiteId(row.getCell(6) == null ? "" : formatter.formatCellValue(row.getCell(6)))
                    .fromPartId(row.getCell(7) == null ? "" : formatter.formatCellValue(row.getCell(7)))
                    .zseq(row.getCell(16) == null ? "" : formatter.formatCellValue(row.getCell(16)))
                    .scenarioId(row.getCell(17) == null ? "" : formatter.formatCellValue(row.getCell(17)))
                    .build();

            Bom bom = Bom.builder()
                    .bomId(bomId)
                    .operationId(row.getCell(2) == null ? "" : formatter.formatCellValue(row.getCell(2)))
                    .bomCategory(row.getCell(3) == null ? "" : formatter.formatCellValue(row.getCell(3)))
                    .outQty(row.getCell(4) == null ? "" : formatter.formatCellValue(row.getCell(4)))
                    .outUom(row.getCell(5) == null ? "" : formatter.formatCellValue(row.getCell(5)))
                    .inQty(row.getCell(8) == null ? "" : formatter.formatCellValue(row.getCell(8)))
                    .inUom(row.getCell(9) == null ? "" : formatter.formatCellValue(row.getCell(9)))
                    .createDatetime(row.getCell(10) == null ? "" : formatter.formatCellValue(row.getCell(10)))
                    .effStartDate(row.getCell(11) == null ? "" : formatter.formatCellValue(row.getCell(11)))
                    .createBy(row.getCell(12) == null ? "" : formatter.formatCellValue(row.getCell(12)))
                    .toPartName(row.getCell(13) == null ? "" : formatter.formatCellValue(row.getCell(13)))
                    .fromPartName(row.getCell(14) == null ? "" : formatter.formatCellValue(row.getCell(14)))
                    .bomText(row.getCell(15) == null ? "" : formatter.formatCellValue(row.getCell(15)))
                    .bomVersion(row.getCell(18) == null ? "" : formatter.formatCellValue(row.getCell(18)))
                    .fromPartLevel(row.getCell(19) == null ? "" : formatter.formatCellValue(row.getCell(19)))
                    .toPartLevel(row.getCell(20) == null ? "" : formatter.formatCellValue(row.getCell(20)))
                    .build();

            bomRepository.save(bom);
        }
        workbook.close();
    }

    public void exportBomExcel(HttpServletResponse response) throws IOException {
        List<Bom> boms = bomRepository.findAll();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("BOM");

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
            row.createCell(0).setCellValue(bom.getBomId().getToSiteId() == null ? "" : bom.getBomId().getToSiteId());
            row.createCell(1).setCellValue(bom.getBomId().getToPartId() == null ? "" : bom.getBomId().getToPartId());
            row.createCell(2).setCellValue(bom.getOperationId() == null ? "" : bom.getOperationId());
            row.createCell(3).setCellValue(bom.getBomCategory() == null ? "" : bom.getBomCategory());
            row.createCell(4).setCellValue(bom.getOutQty() == null ? "" : bom.getOutQty());
            row.createCell(5).setCellValue(bom.getOutUom() == null ? "" : bom.getOutUom());
            row.createCell(6).setCellValue(bom.getBomId().getFromSiteId() == null ? "" : bom.getBomId().getFromSiteId());
            row.createCell(7).setCellValue(bom.getBomId().getFromPartId() == null ? "" : bom.getBomId().getFromPartId());
            row.createCell(8).setCellValue(bom.getInQty() == null ? "" : bom.getInQty());
            row.createCell(9).setCellValue(bom.getInUom() == null ? "" : bom.getInUom());
            row.createCell(10).setCellValue(bom.getCreateDatetime() == null ? "" : bom.getCreateDatetime());
            row.createCell(11).setCellValue(bom.getEffStartDate() == null ? "" : bom.getEffStartDate());
            row.createCell(12).setCellValue(bom.getCreateBy() == null ? "" : bom.getCreateBy());
            row.createCell(13).setCellValue(bom.getToPartName() == null ? "" : bom.getToPartName());
            row.createCell(14).setCellValue(bom.getFromPartName() == null ? "" : bom.getFromPartName());
            row.createCell(15).setCellValue(bom.getBomText() == null ? "" : bom.getBomText());
            row.createCell(16).setCellValue(bom.getBomId().getZseq() == null ? "" : bom.getBomId().getZseq());
            row.createCell(17).setCellValue(bom.getBomId().getScenarioId() == null ? "" : bom.getBomId().getScenarioId());
            row.createCell(18).setCellValue(bom.getBomVersion() == null ? "" : bom.getBomVersion());
            row.createCell(19).setCellValue(bom.getFromPartLevel() == null ? "" : bom.getFromPartLevel());
            row.createCell(20).setCellValue(bom.getToPartLevel() == null ? "" : bom.getToPartLevel());
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=bom_export.xlsx");

        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
