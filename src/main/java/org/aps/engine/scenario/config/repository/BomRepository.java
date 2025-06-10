package org.aps.engine.scenario.config.repository;

import org.aps.engine.scenario.config.entity.Bom;
import org.aps.engine.scenario.config.entity.BomId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("configBomRepository")
public interface BomRepository extends JpaRepository<Bom, BomId> {
}


