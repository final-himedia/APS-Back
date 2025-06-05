package org.aps.analysis.repository;

import org.aps.analysis.entity.Part;
import org.aps.analysis.entity.PartId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartRepository extends JpaRepository<Part, PartId> {

}
