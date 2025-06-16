package org.aps.engine.scenario.bop.repository;

import org.aps.engine.scenario.bop.entity.Operation;
import org.aps.engine.scenario.bop.entity.OperationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("bopOperationRepository")

public interface OperationRepository extends JpaRepository<Operation, OperationId> {
    List<Operation> findByOperationIdScenarioId(String scenarioId);

}
