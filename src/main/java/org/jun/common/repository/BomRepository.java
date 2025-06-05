package org.jun.common.repository;

import org.jun.common.entity.Bom;
import org.jun.common.entity.BomId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BomRepository extends JpaRepository<Bom, BomId> {
}


