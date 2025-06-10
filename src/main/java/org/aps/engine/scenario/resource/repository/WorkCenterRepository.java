package org.aps.engine.scenario.resource.repository;

import org.aps.engine.scenario.resource.entity.WorkCenter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkCenterRepository extends JpaRepository<WorkCenter, String> {
}
