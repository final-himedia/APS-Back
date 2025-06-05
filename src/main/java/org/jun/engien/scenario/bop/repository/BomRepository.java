package org.jun.engien.scenario.bop.repository;

import org.jun.engien.scenario.bop.entity.Bom;
import org.jun.engien.scenario.bop.entity.BomId;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BomRepository extends JpaRepository<Bom, BomId> {
}


