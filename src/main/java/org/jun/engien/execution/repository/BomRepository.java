package org.jun.engien.execution.repository;

import org.jun.engien.execution.entity.Bom;
import org.jun.engien.execution.entity.BomId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BomRepository extends JpaRepository<Bom, BomId> {
}


