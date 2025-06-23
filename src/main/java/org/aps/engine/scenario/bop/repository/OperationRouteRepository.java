package org.aps.engine.scenario.bop.repository;

import org.aps.engine.scenario.bop.entity.OperationRoute;
import org.aps.engine.scenario.bop.entity.OperationRouteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OperationRouteRepository extends JpaRepository<OperationRoute, OperationRouteId> {
    List<OperationRoute> findById_ScenarioId(String scenarioId);
}
