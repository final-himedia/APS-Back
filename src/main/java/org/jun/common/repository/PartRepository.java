package org.jun.common.repository;

import org.jun.common.entity.Part;
import org.jun.common.entity.PartId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartRepository extends JpaRepository<Part, PartId> {

}
