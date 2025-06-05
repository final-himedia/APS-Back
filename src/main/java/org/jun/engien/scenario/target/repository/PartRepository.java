package org.jun.engien.scenario.target.repository;

import org.jun.engien.scenario.target.entity.Part;
import org.jun.engien.scenario.target.entity.PartId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartRepository extends JpaRepository<Part, PartId> {

}
