package org.aps.engine.scenario.target.repository;

import org.aps.engine.scenario.bop.entity.Operation;
import org.aps.engine.scenario.target.entity.Demand;
import org.aps.engine.scenario.target.entity.DemandId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DemandRepository extends JpaRepository<Demand, DemandId> {
    List<Demand> findByDemandIdScenarioId(String scenarioId);


}
