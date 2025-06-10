package org.aps.engine.scenario.bop.repository;

import org.aps.engine.scenario.bop.entity.Demand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("bopDemandRepository")
public interface DemandRepository extends JpaRepository<Demand, Long> {

}
