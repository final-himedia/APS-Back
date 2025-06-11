package org.aps.management.repository;

import org.aps.management.entity.Qna;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QnaRepository extends JpaRepository<Qna, Integer> {

    // 삭제되지 않은 글만 조회
    List<Qna> findByDeletedFalse();

    // 제목 조회
    List<Qna> findByTitle(String title);

    // 작성자 글 조회
    List<Qna> findByWriterId(Long writerId);

}
