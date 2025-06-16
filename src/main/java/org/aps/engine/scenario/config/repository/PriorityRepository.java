package org.aps.engine.scenario.config.repository;

import org.aps.engine.scenario.bop.entity.Bom;
import org.aps.engine.scenario.config.entity.Priority;
import org.aps.engine.scenario.config.entity.PriorityId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("configPriorityRepository")
public interface PriorityRepository extends JpaRepository<Priority, PriorityId> {
    List<Priority> findByPriorityIdScenarioId(String scenarioId);

}


