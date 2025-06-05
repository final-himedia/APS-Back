package org.aps.engine.schedule.repository;

import org.aps.engine.schedule.entity.Part;
import org.aps.engine.schedule.entity.PartId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartRepository extends JpaRepository<Part, PartId> {

}
