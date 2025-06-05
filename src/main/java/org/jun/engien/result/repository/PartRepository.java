package org.jun.engien.result.repository;

import org.jun.engien.result.entity.Part;
import org.jun.engien.result.entity.PartId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartRepository extends JpaRepository<Part, PartId> {

}
