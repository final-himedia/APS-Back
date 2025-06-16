package org.aps.engine.scenario.resource.repository;

import org.aps.engine.scenario.resource.entity.WorkCenter;
import org.aps.engine.scenario.resource.entity.WorkCenterMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("resourceWorkCenterRepository")
public interface WorkCenterRepository extends JpaRepository<WorkCenter, String> {
    List<WorkCenter> findByWorkCenterIdScenarioId(String scenarioId);


}
