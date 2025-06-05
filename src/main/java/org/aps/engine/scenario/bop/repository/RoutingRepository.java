package org.aps.engine.scenario.bop.repository;

import org.aps.engine.scenario.bop.entity.Routing;
import org.aps.engine.scenario.bop.entity.RoutingId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoutingRepository extends JpaRepository<Routing, RoutingId> {

}
