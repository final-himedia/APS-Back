package org.jun.analysis.repository;

import org.jun.analysis.entity.Part;
import org.jun.analysis.entity.PartId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartRepository extends JpaRepository<Part, PartId> {

}
