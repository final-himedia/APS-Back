package org.aps.management.repository;

import org.aps.management.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    // 삭제되지 않은 댓글만 조회
    List<Comment> findByQnaIdAndDeletedFalse(Integer qnaId);

    // 특정 댓글 조회
    Optional<Comment> findByIdAndQnaIdAndDeletedFalse(Integer commentId, Integer qnaId);


}
