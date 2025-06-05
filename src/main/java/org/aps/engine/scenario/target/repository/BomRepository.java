package org.aps.engine.scenario.target.repository;

import org.aps.engine.scenario.target.entity.Bom;
import org.aps.engine.scenario.target.entity.BomId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BomRepository extends JpaRepository<Bom, BomId> {
}


