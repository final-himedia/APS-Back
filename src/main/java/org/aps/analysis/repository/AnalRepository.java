package org.aps.analysis.repository;

import org.aps.analysis.entity.Anal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnalRepository extends JpaRepository<Anal, Long> {
    List<Anal> findAllByScenarioId(String s);

    // 최신순으로 정렬
    List<Anal> findAllByOrderByIdDesc();

}
