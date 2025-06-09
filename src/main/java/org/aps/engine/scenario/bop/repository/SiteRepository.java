package org.aps.engine.scenario.bop.repository;

import org.aps.engine.scenario.bop.entity.Site;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SiteRepository extends JpaRepository<Site, String> {
}
