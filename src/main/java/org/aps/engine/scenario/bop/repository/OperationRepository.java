package org.aps.engine.scenario.bop.repository;

import org.aps.engine.scenario.bop.entity.Operation;
import org.aps.engine.scenario.bop.entity.OperationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("bopOperationRepository")

public interface OperationRepository extends JpaRepository<Operation, OperationId> {

}
