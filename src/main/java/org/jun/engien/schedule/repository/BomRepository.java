package org.jun.engien.schedule.repository;

import org.jun.engien.schedule.entity.Bom;
import org.jun.engien.schedule.entity.BomId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BomRepository extends JpaRepository<Bom, BomId> {
}


