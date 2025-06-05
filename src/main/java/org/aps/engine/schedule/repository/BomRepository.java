package org.aps.engine.schedule.repository;

import org.aps.engine.schedule.entity.Bom;
import org.aps.engine.schedule.entity.BomId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BomRepository extends JpaRepository<Bom, BomId> {
}


