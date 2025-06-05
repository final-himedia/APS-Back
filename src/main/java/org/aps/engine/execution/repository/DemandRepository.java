package org.aps.engine.execution.repository;

import org.aps.engine.execution.entity.Demand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DemandRepository extends JpaRepository<Demand, Long> {

}
