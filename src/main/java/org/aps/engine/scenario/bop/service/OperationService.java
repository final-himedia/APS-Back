package org.aps.engine.scenario.bop.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.aps.engine.scenario.bop.entity.Bom;
import org.aps.engine.scenario.bop.entity.BomId;
import org.aps.engine.scenario.bop.entity.Operation;
import org.aps.engine.scenario.bop.repository.BomRepository;
import org.aps.engine.scenario.bop.repository.OperationRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OperationService {
    private final OperationRepository operationRepository;

    public void excelHandle(MultipartFile file) throws IOException {
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);



        }
    }

    public void exportBomExcel(HttpServletResponse response) throws IOException {

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


        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=bom_export.xlsx");

        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
