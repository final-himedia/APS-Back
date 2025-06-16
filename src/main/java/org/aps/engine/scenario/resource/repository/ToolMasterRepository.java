package org.aps.engine.scenario.resource.repository;

import org.aps.engine.scenario.resource.entity.ToolMap;
import org.aps.engine.scenario.resource.entity.ToolMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("resourceToolMasterRepository")
public interface ToolMasterRepository extends JpaRepository<ToolMaster, String> {
    List<ToolMaster> findByToolMasterIdScenarioId(String scenarioId);


}
