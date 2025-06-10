package org.aps.engine.scenario.resource.repository;

import org.aps.engine.execution.entity.Tool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("resourceToolRepository")
public interface ToolRepository extends JpaRepository<Tool, Long> {
}
