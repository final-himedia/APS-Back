package org.aps.engine.scenario.resource.repository;

import org.aps.engine.scenario.resource.entity.WorkCenterMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("resourceWorkCenterMapRepository")
public interface WorkCenterMapRepository extends JpaRepository<WorkCenterMap, String> {

}
