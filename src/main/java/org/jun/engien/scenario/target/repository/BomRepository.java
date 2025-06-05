package org.jun.engien.scenario.target.repository;

import org.jun.engien.scenario.target.entity.Bom;
import org.jun.engien.scenario.target.entity.BomId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BomRepository extends JpaRepository<Bom, BomId> {
}


