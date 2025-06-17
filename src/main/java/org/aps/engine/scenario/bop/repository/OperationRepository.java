package org.aps.engine.scenario.bop.repository;

import org.aps.engine.execution.response.ExecutionResponse;
import org.aps.engine.scenario.bop.entity.Operation;
import org.aps.engine.scenario.bop.entity.OperationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("bopOperationRepository")

public interface OperationRepository extends JpaRepository<Operation, OperationId> {
    @Query("""
    SELECT new org.aps.engine.execution.response.ExecutionResponse(
        o.operationId.siteId, o.operationId.operationId, o.operationName, o.runTime,
        p.partId.siteId, p.partId.partId, p.partId.partType, p.routingId, p.partName,
        orr.operationRoutingId.siteId, orr.operationRoutingId.routingId, orr.operationRoutingId.operationId, orr.operationName, orr.operationRoutingId.operationSeq, orr.operationType,
        o.operationId.scenarioId
    )
    FROM Operation o
    LEFT JOIN BopPart p ON o.operationId.scenarioId = p.partId.scenarioId
    LEFT JOIN OperationRouting orr ON o.operationId.scenarioId = orr.operationRoutingId.scenarioId
    WHERE o.operationId.scenarioId = :scenarioId
""")
    List<ExecutionResponse> findByScenarioId(@Param("scenarioId") String scenarioId);

    List<Operation> findByOperationIdScenarioId(String scenarioId);
}
