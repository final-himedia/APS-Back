package org.aps.engine.scenario.bop.repository;

import org.aps.engine.scenario.bop.entity.Bom;
import org.aps.engine.scenario.bop.entity.BomId;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BomRepository extends JpaRepository<Bom, BomId> {

}


