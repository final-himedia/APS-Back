package org.jun.analysis.repository;

import org.jun.analysis.entity.Bom;
import org.jun.analysis.entity.BomId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BomRepository extends JpaRepository<Bom, BomId> {
}


