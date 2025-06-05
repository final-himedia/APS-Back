package org.jun.engien.execution.repository;

import org.jun.engien.execution.entity.Part;
import org.jun.engien.execution.entity.PartId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartRepository extends JpaRepository<Part, PartId> {

}
