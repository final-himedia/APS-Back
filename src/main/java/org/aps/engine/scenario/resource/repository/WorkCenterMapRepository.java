package org.aps.engine.scenario.resource.repository;

import org.aps.engine.scenario.resource.entity.WorkCenterMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("resourceWorkCenterMapRepository")
public interface WorkCenterMapRepository extends JpaRepository<WorkCenterMap, String> {
    @Query("SELECT wm FROM WorkCenterMap wm WHERE wm.workCenter.workCenterId.scenarioId = :scenarioId")
    List<WorkCenterMap> findByWorkCenterScenarioId(@Param("scenarioId") String scenarioId);


    List<WorkCenterMap> findByOperationRoute_Id_RoutingIdAndOperationRoute_Id_ScenarioId(String routingId, String scenarioId);
}
