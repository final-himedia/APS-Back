package org.aps.engine.execution.repository;

import org.aps.engine.execution.entity.Part;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("executionPartRepository")
public interface PartRepository extends JpaRepository<Part, String> {
}
