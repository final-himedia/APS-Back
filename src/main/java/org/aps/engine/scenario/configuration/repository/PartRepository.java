package org.aps.engine.scenario.configuration.repository;

import org.aps.engine.scenario.configuration.entity.Part;
import org.aps.engine.scenario.configuration.entity.PartId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartRepository extends JpaRepository<Part, PartId> {

}
