package org.aps.engine.scenario.config.repository;

import org.aps.engine.scenario.config.entity.Priority;
import org.aps.engine.scenario.config.entity.PriorityId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PriorityRepository extends JpaRepository<Priority, PriorityId> {
}


