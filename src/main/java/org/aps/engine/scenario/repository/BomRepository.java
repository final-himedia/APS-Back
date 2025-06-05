package org.aps.engine.scenario.repository;

import org.aps.engine.scenario.entity.Bom;
import org.aps.engine.scenario.entity.BomId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BomRepository extends JpaRepository<Bom, BomId> {
}


