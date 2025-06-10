package org.aps.engine.scenario.bop.repository;

import org.aps.engine.scenario.bop.entity.Demand;
import org.aps.engine.scenario.bop.entity.DemandId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DemandRepository extends JpaRepository<Demand, DemandId> {

}
