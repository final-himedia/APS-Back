package org.aps.engine.scenario.resource.repository;

import org.aps.engine.scenario.resource.entity.Part;
import org.aps.engine.scenario.resource.entity.PartId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartRepository extends JpaRepository<Part, PartId> {

}
