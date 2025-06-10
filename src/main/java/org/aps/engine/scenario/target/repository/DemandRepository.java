package org.aps.engine.scenario.target.repository;

import org.aps.engine.scenario.target.entity.Demand;
import org.aps.engine.scenario.target.entity.DemandId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DemandRepository extends JpaRepository<Demand, DemandId> {

}
