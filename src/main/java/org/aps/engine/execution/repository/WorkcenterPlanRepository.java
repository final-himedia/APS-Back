package org.aps.engine.execution.repository;


<<<<<<< jjun
import org.aps.engine.execution.result.WorkcenterPlan;
=======
import org.aps.engine.result.entity.WorkcenterPlan;
>>>>>>> main
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkcenterPlanRepository extends JpaRepository<WorkcenterPlan, Integer > {


    List<WorkcenterPlan> findAllByScenarioId(String scenarioId);
}
