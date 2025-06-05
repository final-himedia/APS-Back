package org.jun.engien.scenario.repository;

import org.jun.engien.scenario.entity.Bom;
import org.jun.engien.scenario.entity.BomId;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BomRepository extends JpaRepository<Bom, BomId> {
}


