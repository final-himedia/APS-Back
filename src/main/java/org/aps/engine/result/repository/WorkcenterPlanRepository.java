package org.aps.engine.result.repository;

import org.aps.engine.execution.result.WorkcenterPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("resultWorkcenterRepository")
public interface WorkcenterPlanRepository extends JpaRepository<WorkcenterPlan, Integer> {
    List<WorkcenterPlan> findByScenarioId(String scenarioId);

}