package org.aps.analysis.service;

import lombok.RequiredArgsConstructor;
import org.aps.analysis.entity.Anal;
import org.aps.analysis.repository.AnalRepository;
import org.aps.engine.execution.service.ExecutionResultService;
import org.aps.engine.response.AnalResponse;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
    public List<Anal> getAllAnal() {
        return analRepository.findAll();
    }
}


