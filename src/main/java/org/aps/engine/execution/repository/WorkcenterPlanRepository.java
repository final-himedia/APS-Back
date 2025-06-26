package org.aps.engine.execution.repository;


import org.aps.engine.result.entity.workcenterPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkcenterPlanRepository extends JpaRepository<workcenterPlan, Integer > {


    List<workcenterPlan> findAllByScenarioId(String scenarioId);
}
