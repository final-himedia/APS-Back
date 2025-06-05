package org.aps.engine.result.repository;

import org.aps.engine.result.entity.Part;
import org.aps.engine.result.entity.PartId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartRepository extends JpaRepository<Part, PartId> {

}
