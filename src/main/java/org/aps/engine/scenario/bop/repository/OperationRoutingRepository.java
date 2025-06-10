package org.aps.engine.scenario.bop.repository;

import org.aps.engine.scenario.bop.entity.OperationRouting;
import org.aps.engine.scenario.bop.entity.OperationRoutingId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperationRoutingRepository extends JpaRepository<OperationRouting, OperationRoutingId> {

}
