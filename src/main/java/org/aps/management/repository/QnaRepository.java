package org.aps.management.repository;

import org.aps.management.entity.Qna;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QnaRepository extends JpaRepository<Qna, Integer> {

    // 전체 글 목록 최신순 조회
    List<Qna> findByDeletedFalseOrderByWroteAtDesc();

    // 게시글 상세 조회
    Optional<Qna> findByIdAndDeletedFalse(Integer id);

    // 제목 글 검색
    List<Qna> findByTitleContainingAndDeletedFalse(String title);

    // 작성자 글 검색
    List<Qna> findByWriterIdAndDeletedFalse(Long writerId);

    List<Qna> findByCategory(String category);
}