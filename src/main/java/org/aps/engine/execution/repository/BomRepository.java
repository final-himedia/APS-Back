package org.aps.engine.execution.repository;

import org.aps.engine.execution.entity.Bom;
import org.aps.engine.execution.entity.BomId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BomRepository extends JpaRepository<Bom, BomId> {
}


