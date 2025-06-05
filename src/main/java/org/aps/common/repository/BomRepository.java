package org.aps.common.repository;

import org.aps.common.entity.Bom;
import org.aps.common.entity.BomId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BomRepository extends JpaRepository<Bom, BomId> {
}


