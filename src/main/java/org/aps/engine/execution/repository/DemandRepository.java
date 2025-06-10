package org.aps.engine.execution.repository;

import org.aps.engine.execution.entity.Demand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("executionDemandRepository")
public interface DemandRepository extends JpaRepository<Demand, Long> {

}
