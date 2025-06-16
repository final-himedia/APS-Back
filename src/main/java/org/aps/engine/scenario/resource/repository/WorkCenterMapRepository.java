package org.aps.engine.scenario.resource.repository;

import org.aps.engine.scenario.resource.entity.ToolMap;
import org.aps.engine.scenario.resource.entity.WorkCenterMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("resourceWorkCenterMapRepository")
public interface WorkCenterMapRepository extends JpaRepository<WorkCenterMap, String> {
    List<WorkCenterMap> findByWorkCenterMapIdScenarioId(String scenarioId);

}
