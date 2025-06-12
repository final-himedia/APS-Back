package org.aps.engine.scenario.resource.repository;

import org.aps.engine.scenario.resource.entity.ToolMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ToolMapRepository extends JpaRepository<ToolMap, String> {
}
