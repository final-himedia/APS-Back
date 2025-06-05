package org.jun.engien.scenario.resource.repository;

import org.jun.engien.scenario.resource.entity.Part;
import org.jun.engien.scenario.resource.entity.PartId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartRepository extends JpaRepository<Part, PartId> {

}
