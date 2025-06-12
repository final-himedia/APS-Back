package org.aps.management.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.aps.common.entity.User;
import org.aps.management.entity.Comment;
import org.aps.management.entity.Qna;
import org.aps.management.repository.CommentRepository;
import org.aps.management.repository.QnaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

import static org.aps.common.util.LoginUtil.getLoginUser;

@RestController
@RequestMapping("/api/management/qna")
@RequiredArgsConstructor
public class ManagementController {

    private final QnaRepository qnaRepository;
    private final CommentRepository commentRepository;

    // 전체 게시글 목록 조회
    @GetMapping
    public ResponseEntity<List<Qna>> AllQna() {
        List<Qna> qnas = qnaRepository.findByDeletedFalse();
        return ResponseEntity.ok(qnas);
    }

    // 게시글 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<Qna> detailQna(@PathVariable Integer id) {
        Qna qna = qnaRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return ResponseEntity.ok(qna);
    }

    // 게시글 등록
    @PostMapping
    public ResponseEntity<Qna> createQna(@RequestBody Qna request, HttpSession session) {
        User loginUser = getLoginUser(session);

        Qna newQna = Qna.builder()
                .writerId(loginUser.getId())
                .title(request.getTitle())
                .content(request.getContent())
                .wroteAt(LocalDateTime.now())
                .deleted(false)
                .build();

        Qna saved = qnaRepository.save(newQna);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // 게시글 수정
    @PutMapping("/{id}")
    public ResponseEntity<Qna> updateQna(@PathVariable Integer id,
                                         @RequestBody Qna request,
                                         HttpSession session) {
        User loginUser = getLoginUser(session);

        Qna qna = qnaRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!qna.getWriterId().equals(loginUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "작성자만 수정할 수 있습니다.");
        }

        qna.setTitle(request.getTitle());
        qna.setContent(request.getContent());
        return ResponseEntity.ok(qnaRepository.save(qna));
    }

    // 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQna(@PathVariable Integer id, HttpSession session) {
        User loginUser = getLoginUser(session);

        Qna qna = qnaRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!qna.getWriterId().equals(loginUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "작성자만 삭제할 수 있습니다.");
        }

        qna.setDeleted(true);
        qnaRepository.save(qna);

        List<Comment> comments = commentRepository.findByQnaIdAndDeletedFalse(id);
        comments.forEach(c -> c.setDeleted(true));
        commentRepository.saveAll(comments);

        return ResponseEntity.noContent().build();
    }

    // 게시글 검색
    @GetMapping("/search")
    public ResponseEntity<List<Qna>> searchQna(@RequestParam String title) {
        List<Qna> results = qnaRepository.findByTitleAndDeletedFalse(title);
        if (results.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(results);
    }

    // 댓글 목록 조회
    @GetMapping("/{qnaId}/comment")
    public ResponseEntity<List<Comment>> getComments(@PathVariable Integer qnaId) {
        List<Comment> comments = commentRepository.findByQnaIdAndDeletedFalse(qnaId);
        return ResponseEntity.ok(comments);
    }

    // 댓글 등록
    @PostMapping("/{qnaId}/comment")
    public ResponseEntity<Comment> createComment(@PathVariable Integer qnaId,
                                                 @RequestBody Comment request,
                                                 HttpSession session) {
        User loginUser = getLoginUser(session);

        Comment comment = Comment.builder()
                .qnaId(qnaId)
                .writerId(loginUser.getId())
                .content(request.getContent())
                .wroteAt(LocalDateTime.now())
                .deleted(false)
                .build();

        Comment saved = commentRepository.save(comment);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // 댓글 수정
    @PutMapping("/{qnaId}/comment/{commentId}")
    public ResponseEntity<Comment> updateComment(@PathVariable Integer qnaId,
                                                 @PathVariable Integer commentId,
                                                 @RequestBody Comment request,
                                                 HttpSession session) {
        User loginUser = getLoginUser(session);

        Comment comment = commentRepository.findByIdAndQnaIdAndDeletedFalse(commentId, qnaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!comment.getWriterId().equals(loginUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "작성자만 수정할 수 있습니다.");
        }

        comment.setContent(request.getContent());
        return ResponseEntity.ok(commentRepository.save(comment));
    }

    // 댓글 삭제
    @DeleteMapping("/{qnaId}/comment/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Integer qnaId,
                                              @PathVariable Integer commentId,
                                              HttpSession session) {
        User loginUser = getLoginUser(session);

        Comment comment = commentRepository.findByIdAndQnaIdAndDeletedFalse(commentId, qnaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!comment.getWriterId().equals(loginUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "작성자만 삭제할 수 있습니다.");
        }

        comment.setDeleted(true);
        commentRepository.save(comment);
        return ResponseEntity.noContent().build();
    }
}
