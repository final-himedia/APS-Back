package org.aps.analysis.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.aps.analysis.entity.Anal;
import org.aps.analysis.repository.AnalRepository;
import org.aps.engine.execution.service.ExecutionResultService;
import org.aps.engine.response.AnalResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AnalService {
    private final ExecutionResultService executionResultService;
    private final AnalRepository analRepository;

    public Anal runSimulationAndSaveAnal(String scenarioId, String userId) {
        LocalDateTime start = LocalDateTime.now();
        String version = "Experiment_" + start.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        String status = "완료";
        String errorMessage = null;
        LocalDateTime actualStart;
        LocalDateTime actualEnd;

        try {
            AnalResponse result = executionResultService.simulateSchedule(scenarioId);
            actualStart = result.getStartTime();
            actualEnd = result.getEndTime();
        } catch (Exception e) {
            status = "실패";
            errorMessage = e.getMessage();
            actualStart = start;
            actualEnd = LocalDateTime.now();
        }

        Anal anal = Anal.builder()
                .scenarioId(scenarioId) // ← 필요시
                .version(version)
                .userId(userId)
                .startTime(actualStart)
                .endTime(actualEnd)
                .durationMinutes(Duration.between(actualStart, actualEnd).toMinutes())
                .status(status)
                .errorMessage(errorMessage)
                .build();

        return analRepository.save(anal);
    }

    // 실행 이력 전체 조회
    public Map<String, Object> getAnalByScenarioId(String scenarioId) {
        List<Anal> anals = analRepository.findAllByScenarioId(scenarioId);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", 200);
        response.put("analList", anals);
        response.put("total", anals.size());

        return response;
    }

    // 실행 이력 다운로드
    public void exportAnalExcel(String scenarioId, HttpServletResponse response) throws IOException {
        List<Anal> anals = analRepository.findAllByScenarioId(scenarioId);
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("ANAL_" + scenarioId);

        String[] headers = {
                "ID", "Scenario ID", "User ID", "Version", "Status",
                "Start Time", "End Time", "Duration (min)", "Error Message"
        };

        Row header = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            header.createCell(i).setCellValue(headers[i]);
        }

        int rowIdx = 1;
        for (Anal anal : anals) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(anal.getId() != null ? anal.getId() : 0);
            row.createCell(1).setCellValue(anal.getScenarioId() != null ? anal.getScenarioId() : "");
            row.createCell(2).setCellValue(anal.getUserId() != null ? anal.getUserId() : "");
            row.createCell(3).setCellValue(anal.getVersion() != null ? anal.getVersion() : "");
            row.createCell(4).setCellValue(anal.getStatus() != null ? anal.getStatus() : "");
            row.createCell(5).setCellValue(anal.getStartTime() != null ? anal.getStartTime().toString() : "");
            row.createCell(6).setCellValue(anal.getEndTime() != null ? anal.getEndTime().toString() : "");
            row.createCell(7).setCellValue(anal.getDurationMinutes() != null ? anal.getDurationMinutes() : 0);
            row.createCell(8).setCellValue(anal.getErrorMessage() != null ? anal.getErrorMessage() : "");
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=anal_result_" + scenarioId + ".xlsx");

        workbook.write(response.getOutputStream());
        workbook.close();
    }

    // 식행 이력 삭제
    public Map<String, Object> deleteAnal(String scenarioId) {
        List<Anal> deleteList = analRepository.findAllByScenarioId(scenarioId);
        analRepository.deleteAll(deleteList);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", 200);
        response.put("message", "삭제 완료");
        response.put("deleted", deleteList.size());

        return response;
    }
}


