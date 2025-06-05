package org.jun.engien.result.repository;

import org.jun.engien.result.entity.Bom;
import org.jun.engien.result.entity.BomId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BomRepository extends JpaRepository<Bom, BomId> {
}


