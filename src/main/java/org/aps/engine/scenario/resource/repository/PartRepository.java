package org.aps.engine.scenario.resource.repository;

import org.aps.engine.scenario.resource.entity.Part;
import org.aps.engine.scenario.resource.entity.PartId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("resourcePartRepository")
public interface PartRepository extends JpaRepository<Part, PartId> {

}
