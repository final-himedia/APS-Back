package org.aps.engine.scenario.resource.repository;

import org.aps.engine.scenario.resource.entity.ToolMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("resourceToolMapRepository")
public interface ToolMapRepository extends JpaRepository<ToolMap, String> {


    List<ToolMap> findByScenarioId(String scenarioId);
}
