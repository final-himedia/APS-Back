package org.aps.engine.scenario.bop.repository;

import org.aps.engine.scenario.bop.entity.OperationRouting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("bopOperaionRoutingRepository")
public interface OperationRoutingRepository extends JpaRepository<OperationRouting, Long> {

}
