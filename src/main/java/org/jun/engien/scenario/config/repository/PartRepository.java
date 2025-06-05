package org.jun.engien.scenario.config.repository;

import org.jun.engien.scenario.config.entity.Part;
import org.jun.engien.scenario.config.entity.PartId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartRepository extends JpaRepository<Part, PartId> {

}
