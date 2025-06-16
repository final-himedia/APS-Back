package org.aps.engine.scenario.bop.repository;

import org.aps.engine.scenario.bop.entity.Operation;
import org.aps.engine.scenario.bop.entity.Routing;
import org.aps.engine.scenario.bop.entity.RoutingId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("bopRoutingRepository")
public interface RoutingRepository extends JpaRepository<Routing, RoutingId> {
    List<Routing> findByRoutingIdScenarioId(String scenarioId);

}
