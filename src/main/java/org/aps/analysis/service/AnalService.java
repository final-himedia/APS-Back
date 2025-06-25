package org.aps.analysis.service;

import lombok.RequiredArgsConstructor;
import org.aps.analysis.entity.Anal;
import org.aps.analysis.repository.AnalRepository;
import org.aps.engine.execution.service.ExecutionResultService;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
        LocalDateTime end;

        try {
            executionResultService.simulateSchedule(scenarioId);
            end = LocalDateTime.now();
        } catch (Exception e) {
            status = "실패";
            errorMessage = e.getMessage();
            end = LocalDateTime.now();
        }

        Anal anal = Anal.builder()
                .version(version)
                .userId(userId)
                .startTime(start)
                .endTime(end)
                .status(status)
                .errorMessage(errorMessage)
                .durationMinutes(Duration.between(start, end).toMinutes())
                .build();


        return analRepository.save(anal);
    }
}


