package org.jun.engien.schedule.repository;

import org.jun.engien.schedule.entity.Part;
import org.jun.engien.schedule.entity.PartId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartRepository extends JpaRepository<Part, PartId> {

}
