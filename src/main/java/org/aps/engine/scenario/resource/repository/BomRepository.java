package org.aps.engine.scenario.resource.repository;

import org.aps.engine.scenario.resource.entity.Bom;
import org.aps.engine.scenario.resource.entity.BomId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("resourceBomRepository")
public interface BomRepository extends JpaRepository<Bom, BomId> {
}


