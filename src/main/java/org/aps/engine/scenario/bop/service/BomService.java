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
                    .fromSiteId(formatter.formatCellValue(row.getCell(5)))
                    .fromPartId(formatter.formatCellValue(row.getCell(6)))
                    .zseq(formatter.formatCellValue(row.getCell(12)))
                    .build();

            Bom bom = Bom.builder()
                    .bomId(bomId)
                    .bomCategory(formatter.formatCellValue(row.getCell(3)))
                    .outQty(formatter.formatCellValue(row.getCell(4)))
                    .inQty(formatter.formatCellValue(row.getCell(7)))
                    .createBy(formatter.formatCellValue(row.getCell(8)))
                    .toPartName(formatter.formatCellValue(row.getCell(9)))
                    .fromPartName(formatter.formatCellValue(row.getCell(10)))
                    .bomText(formatter.formatCellValue(row.getCell(11)))
                    .scenarioId(formatter.formatCellValue(row.getCell(13)))
                    .bomVersion(formatter.formatCellValue(row.getCell(14)))
                    .fromPartLevel(formatter.formatCellValue(row.getCell(15)))
                    .toPartLevel(formatter.formatCellValue(row.getCell(16)))
                    .build();

            bomRepository.save(bom);
        }
    }


    public void exportBomExcel(HttpServletResponse response) throws IOException {
        List<Bom> boms = bomRepository.findAll();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("BOM");

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("to_site_id");
        header.createCell(1).setCellValue("to_part_id");
        header.createCell(2).setCellValue("bom_category");
        header.createCell(3).setCellValue("out_qty");
        header.createCell(4).setCellValue("from_site_id");
        header.createCell(5).setCellValue("from_part_id");
        header.createCell(6).setCellValue("in_qty");
        header.createCell(7).setCellValue("create_by");
        header.createCell(8).setCellValue("to_part_name");
        header.createCell(9).setCellValue("from_part_name");
        header.createCell(10).setCellValue("bom_text");
        header.createCell(11).setCellValue("zseq");
        header.createCell(12).setCellValue("scenario_id");
        header.createCell(13).setCellValue("bom_version");
        header.createCell(14).setCellValue("from_part_level");
        header.createCell(15).setCellValue("to_part_level");

        int rowIdx = 1;
        for (Bom bom : boms) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(bom.getBomId().getToSiteId());
            row.createCell(1).setCellValue(bom.getBomId().getToPartId());
            row.createCell(2).setCellValue(bom.getBomCategory());
            row.createCell(3).setCellValue(bom.getOutQty());
            row.createCell(4).setCellValue(bom.getBomId().getFromSiteId());
            row.createCell(5).setCellValue(bom.getBomId().getFromPartId());
            row.createCell(6).setCellValue(bom.getInQty());
            row.createCell(7).setCellValue(bom.getCreateBy());
            row.createCell(8).setCellValue(bom.getToPartName());
            row.createCell(9).setCellValue(bom.getFromPartName());
            row.createCell(10).setCellValue(bom.getBomText());
            row.createCell(11).setCellValue(bom.getBomId().getZseq());
            row.createCell(12).setCellValue(bom.getScenarioId());
            row.createCell(13).setCellValue(bom.getBomVersion());
            row.createCell(14).setCellValue(bom.getFromPartLevel());
            row.createCell(15).setCellValue(bom.getToPartLevel());
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=bom_export.xlsx");

        workbook.write(response.getOutputStream());
        workbook.close();
    }


}
