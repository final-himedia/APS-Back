package org.jun.project.repository;

import org.jun.project.entity.Part;
import org.jun.project.entity.PartId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartRepository extends JpaRepository<Part, PartId> {

}
