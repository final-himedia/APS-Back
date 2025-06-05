package org.aps.engine.scenario.repository;

import org.aps.engine.scenario.entity.Part;
import org.aps.engine.scenario.entity.PartId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartRepository extends JpaRepository<Part, PartId> {

}
