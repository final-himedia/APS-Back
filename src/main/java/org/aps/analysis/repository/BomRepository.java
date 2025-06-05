package org.aps.analysis.repository;

import org.aps.analysis.entity.Bom;
import org.aps.analysis.entity.BomId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BomRepository extends JpaRepository<Bom, BomId> {
}


