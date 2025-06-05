package org.aps.engine.execution.repository;

import org.aps.engine.execution.entity.Workcenter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkcenterRepository extends JpaRepository<Workcenter, String> {
}
