package org.aps.management.controller;

import jakarta.servlet.http.HttpServletRequest;
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
        return ResponseEntity.status(200).body(qnas);
    }

    // 게시글 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<Qna> detailQna(@PathVariable Integer id) {
        Qna qna = qnaRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 게시글이 존재하지 않습니다."));

        return ResponseEntity.status(200).body(qna);
    }

    // 게시글 등록
    @PostMapping
    public ResponseEntity<Qna> createQna(@RequestBody Qna request, HttpServletRequest httpRequest) {
        User loginUser = (User) httpRequest.getAttribute("user");

        // 새 게시글 생성
        Qna newQna = Qna.builder()
                .writerId(loginUser.getId())
                .title(request.getTitle())
                .content(request.getContent())
                .wroteAt(LocalDateTime.now())
                .deleted(false)
                .build();

        Qna saved = qnaRepository.save(newQna);
        return ResponseEntity.status(201).body(saved);
    }

    // 게시글 수정
    @PutMapping("/{id}")
    public ResponseEntity<Qna> updateQna(@PathVariable Integer id,
                                         @RequestBody Qna request,
                                         HttpServletRequest httpRequest) {
        User loginUser = (User) httpRequest.getAttribute("user");

        // 해당 ID의 게시글 찾기
        Qna qna = qnaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 게시글을 찾을 수 없습니다."));

        // 삭제된 글은 수정 불가
        if (Boolean.TRUE.equals(qna.getDeleted())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 삭제된 게시글입니다.");
        }

        // 본인만 수정 가능
        if (!loginUser.getId().equals(qna.getWriterId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "수정 권한이 없습니다.");
        }

        qna.setTitle(request.getTitle());
        qna.setContent(request.getContent());
        return ResponseEntity.status(200).body(qnaRepository.save(qna));
    }

    // 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQna(@PathVariable Integer id, HttpServletRequest httpRequest) {
        User loginUser = (User) httpRequest.getAttribute("user");

        Qna qna = qnaRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 게시글이 존재하지 않습니다."));

        // 본인만 삭제 가능
        if (!loginUser.getId().equals(qna.getWriterId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "삭제 권한이 없습니다.");
        }

        qna.setDeleted(true);
        qnaRepository.save(qna);

        // 게시글 및 댓글 삭제
        List<Comment> comments = commentRepository.findByQnaIdAndDeletedFalse(id);
        for (Comment comment : comments) {
            comment.setDeleted(true);
        }
        commentRepository.saveAll(comments);

        return ResponseEntity.status(204).build();
    }

    // 게시글 검색
    @GetMapping("/search")
    public ResponseEntity<List<Qna>> searchQna(@RequestParam String title) {
        List<Qna> results = qnaRepository.findByTitleAndDeletedFalse(title);
        if (results.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.status(200).body(results);
    }

    // 댓글 목록 조회
    @GetMapping("/{qnaId}/comment")
    public ResponseEntity<List<Comment>> getComments(@PathVariable Integer qnaId) {
        List<Comment> comments = commentRepository.findByQnaIdAndDeletedFalse(qnaId);
        return ResponseEntity.status(200).body(comments);
    }

    // 댓글 등록
    @PostMapping("/{qnaId}/comment")
    public ResponseEntity<Comment> createComment(@PathVariable Integer qnaId,
                                                 @RequestBody Comment request,
                                                 HttpServletRequest httpRequest) {

        Qna qna = qnaRepository.findByIdAndDeletedFalse(qnaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 게시글입니다."));

        User loginUser = (User) httpRequest.getAttribute("user");

        Comment comment = Comment.builder()
                .qnaId(qnaId)
                .writerId(loginUser.getId())
                .content(request.getContent())
                .wroteAt(LocalDateTime.now())
                .deleted(false)
                .build();

        Comment saved = commentRepository.save(comment);
        return ResponseEntity.status(201).body(saved);
    }

    // 댓글 수정
    @PutMapping("/{qnaId}/comment/{commentId}")
    public ResponseEntity<Comment> updateComment(@PathVariable Integer qnaId,
                                                 @PathVariable Integer commentId,
                                                 @RequestBody Comment request,
                                                 HttpServletRequest httpRequest) {
        User loginUser = (User) httpRequest.getAttribute("user");

        Comment comment = commentRepository.findByIdAndQnaIdAndDeletedFalse(commentId, qnaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다."));

        if (!loginUser.getId().equals(comment.getWriterId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "수정 권한이 없습니다.");
        }

        comment.setContent(request.getContent());
        return ResponseEntity.status(200).body(commentRepository.save(comment));
    }

    // 댓글 삭제
    @DeleteMapping("/{qnaId}/comment/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Integer qnaId,
                                              @PathVariable Integer commentId,
                                              HttpServletRequest httpRequest) {
        User loginUser = (User) httpRequest.getAttribute("user");

        Comment comment = commentRepository.findByIdAndQnaIdAndDeletedFalse(commentId, qnaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다."));

        if (!loginUser.getId().equals(comment.getWriterId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "삭제 권한이 없습니다.");
        }

        comment.setDeleted(true);
        commentRepository.save(comment);
        return ResponseEntity.status(204).build();
    }
}
