package org.aps.engine.scenario.bop.repository;

import org.aps.engine.execution.response.ExecutionResponse;
import org.aps.engine.scenario.bop.entity.OperationRoute;
import org.aps.engine.scenario.bop.entity.OperationRouteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OperationRouteRepository extends JpaRepository<OperationRoute, OperationRouteId> {
    @Query("""
        SELECT 
            o.id.operationId AS operationId,
            o.operationName AS operationName,
            CAST(o.runTime AS string) AS runTime,

            p.partId.siteId AS partSiteId,
            p.partId.partId AS partId,
            p.partId.partType AS partType,
            o.routingId AS routingId,
            p.partName AS partName,

            o.id.scenarioId AS scenarioId
        FROM OperationRoute o
        LEFT JOIN BopPart p
          ON o.id.scenarioId = p.partId.scenarioId
         AND o.id.siteId = p.partId.siteId
         AND o.routingId = p.routingId
        WHERE o.id.scenarioId = :scenarioId
    """)
    List<ExecutionResponse> findByScenarioIdWithPart(@Param("scenarioId") String scenarioId);
}
