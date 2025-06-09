package org.aps.engine.scenario.config.repository;

import org.aps.engine.scenario.config.entity.Priority;
import org.aps.engine.scenario.config.entity.PriorityId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriorityRepository extends JpaRepository<Priority, PriorityId> {
}


