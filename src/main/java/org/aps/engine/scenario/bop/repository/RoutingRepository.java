package org.aps.engine.scenario.bop.repository;

import org.aps.engine.scenario.bop.entity.Routing;
import org.aps.engine.scenario.bop.entity.RoutingId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("bopRoutingRepository")
public interface RoutingRepository extends JpaRepository<Routing, RoutingId> {

}
