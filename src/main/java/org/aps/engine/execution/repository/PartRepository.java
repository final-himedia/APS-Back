package org.aps.engine.execution.repository;

import org.aps.engine.execution.entity.Part;
import org.aps.engine.execution.entity.PartId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartRepository extends JpaRepository<Part, PartId> {

}
