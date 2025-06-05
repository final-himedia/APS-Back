package org.aps.engine.scenario.configuration.repository;

import org.aps.engine.scenario.configuration.entity.Bom;
import org.aps.engine.scenario.configuration.entity.BomId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BomRepository extends JpaRepository<Bom, BomId> {
}


