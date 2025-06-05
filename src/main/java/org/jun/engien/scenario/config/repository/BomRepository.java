package org.jun.engien.scenario.config.repository;

import org.jun.engien.scenario.config.entity.Bom;
import org.jun.engien.scenario.config.entity.BomId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BomRepository extends JpaRepository<Bom, BomId> {
}


