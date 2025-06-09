package org.aps.engine.scenario.resource.repository;

import org.aps.engine.execution.entity.Operation;
import org.aps.engine.execution.entity.WorkcenterMap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkcenterMapRepository extends JpaRepository<WorkcenterMap, Integer> {
    List<WorkcenterMap> findByOperation(Operation operation);
}
