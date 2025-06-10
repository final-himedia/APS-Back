package org.aps.engine.scenario.resource.repository;

import org.aps.engine.execution.entity.Operation;
import org.aps.engine.execution.entity.WorkcenterMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("resourceWorkcenterMapRepository")
public interface WorkcenterMapRepository extends JpaRepository<WorkcenterMap, Integer> {
    List<WorkcenterMap> findByOperation(Operation operation);
}
