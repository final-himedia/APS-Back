package org.aps.management.repository;

import org.aps.management.entity.Qna;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QnaRepository extends JpaRepository<Qna, Integer> {

    // 삭제되지 않은 글만 조회
    List<Qna> findByDeletedFalse();

    // 제목 조회
    List<Qna> findByTitleAndDeletedFalse(String title);

    // 작성자 글 조회
    Optional<Qna> findByIdAndDeletedFalse(Integer id);

}
