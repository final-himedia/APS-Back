package org.aps.engine.scenario.bop.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.aps.engine.scenario.bop.entity.Bom;
import org.aps.engine.scenario.bop.entity.BomId;
import org.aps.engine.scenario.bop.entity.Site;
import org.aps.engine.scenario.bop.repository.BomRepository;
import org.aps.engine.scenario.bop.repository.SiteRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SiteService {
    private final SiteRepository siteRepository;

    public void excelHandle(MultipartFile file) throws IOException {
        DataFormatter formatter = new DataFormatter();
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            Site site = Site.builder()
                    .siteId(formatter.formatCellValue(row.getCell(0)))
                    .siteName(formatter.formatCellValue(row.getCell(1)))
                    .scenarioId(formatter.formatCellValue(row.getCell(2)))
                    .build();

            siteRepository.save(site);
        }
    }

    public void exportSiteExcel(HttpServletResponse response) throws IOException {
        List<Site> sites = siteRepository.findAll();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("SITE");

        String[] headers = { "site_id", "site_name", "scenario_id" };

        Row header = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            header.createCell(i).setCellValue(headers[i]);
        }

        int rowIdx = 1;
        for (Site site : sites) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(site.getSiteId());
            row.createCell(1).setCellValue(site.getSiteName());
            row.createCell(2).setCellValue(site.getScenarioId());
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=site_export.xlsx");

        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
