package org.aps.management.repository;

import org.aps.management.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {


    // 삭제되지 않은 댓글만 최신순으로 조회
    List<Comment> findByQnaIdAndDeletedFalseOrderByWroteAtDesc(Integer qnaId);
    List<Comment> findByWriterId(Long writerId);

    // 특정 댓글 조회
    Optional<Comment> findByIdAndQnaIdAndDeletedFalse(Integer commentId, Integer qnaId);

}
