package org.aps.engine.result.repository;

import org.aps.engine.result.entity.Bom;
import org.aps.engine.result.entity.BomId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BomRepository extends JpaRepository<Bom, BomId> {
}


