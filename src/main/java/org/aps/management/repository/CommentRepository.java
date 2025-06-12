package org.aps.management.repository;

import org.aps.management.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    // 삭제되지 않은 댓글만 조회
    List<Comment> findByQnaIdAndDeletedFalse(Integer qnaId);

    // 특정 게시글에 달린 댓글 조회
    List<Comment> findByQnaId(Integer qnaId);

}
