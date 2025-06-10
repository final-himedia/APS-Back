package org.aps.engine.scenario.bop.repository;

import org.aps.engine.scenario.bop.entity.Part;
import org.aps.engine.scenario.bop.entity.PartId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("bopPartRepository")
public interface PartRepository extends JpaRepository<Part, PartId> {


}
