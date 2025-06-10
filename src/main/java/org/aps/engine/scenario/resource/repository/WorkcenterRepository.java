package org.aps.engine.scenario.resource.repository;

import org.aps.engine.execution.entity.Workcenter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("resourceWorkcenterRepository")
public interface WorkcenterRepository extends JpaRepository<Workcenter, String> {
}
