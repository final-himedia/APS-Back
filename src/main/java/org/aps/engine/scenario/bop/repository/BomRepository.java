package org.aps.engine.scenario.bop.repository;

import org.aps.engine.scenario.bop.entity.Bom;
import org.aps.engine.scenario.bop.entity.BomId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("bopBomRepository")
public interface BomRepository extends JpaRepository<Bom, BomId> {
    List<Bom> findByBomIdScenarioId(String scenarioId);
}


