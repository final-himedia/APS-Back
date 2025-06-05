package org.aps.engine.execution.repository;

import org.aps.engine.execution.entity.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperationRepository extends JpaRepository<Operation, String> {
}
