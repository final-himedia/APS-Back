package org.aps.engine.scenario.resource.repository;

import org.aps.engine.scenario.resource.entity.ToolMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ToolMasterRepository extends JpaRepository<ToolMaster, String> {

}
