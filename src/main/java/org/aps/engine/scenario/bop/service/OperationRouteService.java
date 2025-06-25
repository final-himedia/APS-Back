package org.aps.engine.scenario.bop.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.aps.engine.scenario.bop.entity.OperationRoute;
import org.aps.engine.scenario.bop.entity.OperationRouteId;
import org.aps.engine.scenario.bop.entity.Routing;
import org.aps.engine.scenario.bop.entity.RoutingId;
import org.aps.engine.scenario.bop.repository.OperationRouteRepository;
import org.aps.engine.scenario.bop.repository.RoutingRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OperationRouteService {
    private final OperationRouteRepository operationRouteRepository;

    public void operationRouteExcelHandle(MultipartFile file, String scenarioId) throws IOException {
        DataFormatter formatter = new DataFormatter();
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            String siteId = formatter.formatCellValue(row.getCell(0));
            String operationId = formatter.formatCellValue(row.getCell(1));
            String routingId = formatter.formatCellValue(row.getCell(2));
            String operationName = formatter.formatCellValue(row.getCell(3));
            Integer runTime = Integer.valueOf(formatter.formatCellValue(row.getCell(4)));
            String runTimeUom = formatter.formatCellValue(row.getCell(5));
            Integer waitTime = Integer.valueOf(formatter.formatCellValue(row.getCell(6)));
            String waitTimeUom = formatter.formatCellValue(row.getCell(7));
            Integer transferTime = Integer.valueOf(formatter.formatCellValue(row.getCell(8)));
            String transferTimeUom = formatter.formatCellValue(row.getCell(9));
            Integer operationSeq = Integer.valueOf(formatter.formatCellValue(row.getCell(10)));
            String operationType = formatter.formatCellValue(row.getCell(11));
            String sourcingType = formatter.formatCellValue(row.getCell(12));
            Double yield = Double.valueOf(formatter.formatCellValue(row.getCell(13)));

            OperationRouteId id = OperationRouteId.builder()
                    .siteId(siteId)
                    .operationId(operationId)
                    .scenarioId(scenarioId)
                    .build();

            OperationRoute operationRoute = OperationRoute.builder()
                    .id(id)
                    .routingId(routingId)
                    .operationName(operationName)
                    .runTime(runTime)
                    .runTimeUom(runTimeUom)
                    .waitTime(waitTime)
                    .waitTimeUom(waitTimeUom)
                    .transferTime(transferTime)
                    .transferTimeUom(transferTimeUom)
                    .operationSeq(operationSeq)
                    .operationType(operationType)
                    .sourcingType(sourcingType)
                    .yield(yield)
                    .build();

            operationRouteRepository.save(operationRoute);
        }

        workbook.close();
    }

    public void exportOperationRouteExcel(String scenarioId, HttpServletResponse response) throws IOException {
        List<OperationRoute> routes = operationRouteRepository.findById_ScenarioIdOrderByOperationSeq(scenarioId);

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("OPERATION_ROUTE");

        String[] headers = {
                "site_id", "operation_id", "routing_id", "operation_name",
                "run_time", "run_time_uom", "wait_time", "wait_time_uom",
                "transfer_time", "transfer_time_uom", "operation_seq", "operation_type",
                "sourcing_type", "yield"
        };

        Row header = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            header.createCell(i).setCellValue(headers[i]);
        }

        int rowIdx = 1;
        for (OperationRoute route : routes) {
            Row row = sheet.createRow(rowIdx++);
            OperationRouteId id = route.getId();

            row.createCell(0).setCellValue(id.getSiteId());
            row.createCell(1).setCellValue(id.getOperationId());
            row.createCell(2).setCellValue(route.getRoutingId());
            row.createCell(3).setCellValue(route.getOperationName());
            row.createCell(4).setCellValue(route.getRunTime() != null ? route.getRunTime() : 0);
            row.createCell(5).setCellValue(route.getRunTimeUom());
            row.createCell(6).setCellValue(route.getWaitTime() != null ? route.getWaitTime() : 0);
            row.createCell(7).setCellValue(route.getWaitTimeUom());
            row.createCell(8).setCellValue(route.getTransferTime() != null ? route.getTransferTime() : 0);
            row.createCell(9).setCellValue(route.getTransferTimeUom());
            row.createCell(10).setCellValue(route.getOperationSeq() != null ? route.getOperationSeq() : 0);
            row.createCell(11).setCellValue(route.getOperationType());
            row.createCell(12).setCellValue(route.getSourcingType());
            row.createCell(13).setCellValue(route.getYield() != null ? route.getYield() : 0.0);
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=operation_route_export.xlsx");

        workbook.write(response.getOutputStream());
        workbook.close();
    }

}
