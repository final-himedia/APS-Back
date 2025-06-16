package org.aps.engine.scenario.resource.repository;

import org.aps.engine.scenario.bop.entity.Bom;
import org.aps.engine.scenario.resource.entity.ToolMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("resourceToolMapRepository")
public interface ToolMapRepository extends JpaRepository<ToolMap, String> {
    List<ToolMap> findByToolMapIdScenarioId(String scenarioId);

}
