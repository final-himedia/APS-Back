package org.jun.engien.scenario.repository;

import org.jun.engien.scenario.entity.Part;
import org.jun.engien.scenario.entity.PartId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartRepository extends JpaRepository<Part, PartId> {

}
