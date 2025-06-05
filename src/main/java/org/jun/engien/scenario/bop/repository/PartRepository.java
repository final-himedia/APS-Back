package org.jun.engien.scenario.bop.repository;

import org.jun.engien.scenario.bop.entity.Part;
import org.jun.engien.scenario.bop.entity.PartId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartRepository extends JpaRepository<Part, PartId> {

}
