package org.aps.engine.execution.repository;


import org.aps.engine.execution.result.WorkcenterPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WorkcenterPlanRepository extends JpaRepository<WorkcenterPlan, Integer > {


    List<WorkcenterPlan> findAllByScenarioId(String scenarioId);

    // 시나리오 ID가 일치하고, 작업 시간이 지정된 기간(start~end) 내에 있는 작업 계획 조회
    List<WorkcenterPlan> findByScenarioIdAndWorkcenterStartTimeGreaterThanEqualAndWorkcenterEndTimeLessThanEqual(
            String scenarioId, LocalDateTime startTime, LocalDateTime endTime
    );
}
