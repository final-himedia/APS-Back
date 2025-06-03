package org.jun.project.repository;

import org.jun.project.entity.Bom;
import org.jun.project.entity.BomId;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BomRepository extends JpaRepository<Bom, BomId> {
}


