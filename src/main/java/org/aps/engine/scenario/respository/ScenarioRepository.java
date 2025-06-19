package org.aps.engine.scenario.respository;

import org.aps.engine.scenario.entity.Scenario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScenarioRepository extends JpaRepository<Scenario, String> {

}
