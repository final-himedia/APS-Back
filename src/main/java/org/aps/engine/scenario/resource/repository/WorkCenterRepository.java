package org.aps.engine.scenario.resource.repository;

import org.aps.engine.scenario.resource.entity.WorkCenter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkCenterRepository extends JpaRepository<WorkCenter, String> {

}
