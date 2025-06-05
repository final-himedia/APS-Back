package org.aps.engine.scenario.target.repository;

import org.aps.engine.scenario.target.entity.Part;
import org.aps.engine.scenario.target.entity.PartId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartRepository extends JpaRepository<Part, PartId> {

}
