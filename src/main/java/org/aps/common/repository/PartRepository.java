package org.aps.common.repository;

import org.aps.common.entity.Part;
import org.aps.common.entity.PartId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartRepository extends JpaRepository<Part, PartId> {

}
