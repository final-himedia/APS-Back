package org.aps.engine.scenario.target.repository;

import org.aps.engine.scenario.resource.entity.ToolMap;
import org.aps.engine.scenario.target.entity.Demand;
import org.aps.engine.scenario.target.entity.DemandId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository("targetDemandRepository")
public interface DemandRepository extends JpaRepository<Demand, DemandId> {
    List<Demand> findByDemandIdScenarioId(String scenarioId);

}
