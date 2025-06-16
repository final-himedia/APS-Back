package org.aps.engine.scenario.bop.repository;

import org.aps.engine.scenario.bop.entity.OperationRouting;
import org.aps.engine.scenario.bop.entity.OperationRoutingId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("bopOperationRoutingRepository")
public interface OperationRoutingRepository extends JpaRepository<OperationRouting, OperationRoutingId> {
    List<OperationRouting> findByOperationRoutingIdScenarioId(String scenarioId);

}
