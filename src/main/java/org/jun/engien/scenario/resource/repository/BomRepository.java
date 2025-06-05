package org.jun.engien.scenario.resource.repository;

import org.jun.engien.scenario.resource.entity.Bom;
import org.jun.engien.scenario.resource.entity.BomId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BomRepository extends JpaRepository<Bom, BomId> {
}


