package org.aps.engine.scenario.config.repository;

import org.aps.engine.scenario.config.entity.Part;
import org.aps.engine.scenario.config.entity.PartId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartRepository extends JpaRepository<Part, PartId> {

}
