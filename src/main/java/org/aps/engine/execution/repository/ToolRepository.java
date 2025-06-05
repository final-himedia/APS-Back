package org.aps.engine.execution.repository;

import org.aps.engine.execution.entity.Tool;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ToolRepository extends JpaRepository<Tool, Long> {
}
