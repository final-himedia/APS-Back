package org.aps.analysis.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AnalRequest {
    private String scenarioId;
    private LocalDate startDate;
    private LocalDate endDate;
}
