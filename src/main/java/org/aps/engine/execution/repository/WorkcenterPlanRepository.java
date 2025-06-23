package org.aps.engine.execution.repository;

import org.aps.engine.execution.result.WorkcenterPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WorkcenterPlanRepository extends JpaRepository<WorkcenterPlan, Integer > {

}
