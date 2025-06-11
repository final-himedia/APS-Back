package org.aps.management.controller;

import lombok.RequiredArgsConstructor;
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


    // 전체 게시글 조회
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

    // 새 게시글 등록
    @PostMapping
    public ResponseEntity<Qna> createQna(@RequestBody Qna request) {
        Qna newQna = Qna.builder()
                .writerId(request.getWriterId())
                .title(request.getTitle())
                .content(request.getContent())
                .wroteAt(LocalDateTime.now())
                .deleted(false)
                .build();

        Qna savedQna = qnaRepository.save(newQna);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedQna);
    }

    // 게시글 수정
    @PutMapping("/{id}")
    public ResponseEntity<Qna> updateQna(@PathVariable Integer id, @RequestBody Qna request) {
        Qna qna = qnaRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        qna.setTitle(request.getTitle());
        qna.setContent(request.getContent());

        Qna updatedQna = qnaRepository.save(qna);
        return ResponseEntity.ok(updatedQna);
    }

    // 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQna(@PathVariable Integer id) {
        Qna qna = qnaRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        qna.setDeleted(true);
        qnaRepository.save(qna);

        // 해당 게시글에 달린 댓글 삭제
        List<Comment> comments = commentRepository.findByQnaIdAndDeletedFalse(id);
        for (Comment comment : comments) {
            comment.setDeleted(true);
        }
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

    // 댓글 등록
    @PostMapping("/{qnaId}/comment")
    public ResponseEntity<Comment> createComment(@PathVariable Integer qnaId,
                                                 @RequestBody Comment request) {

        Comment comment = Comment.builder()
                .qnaId(qnaId)
                .writerId(request.getWriterId())
                .content(request.getContent())
                .wroteAt(LocalDateTime.now())
                .deleted(false)
                .build();

        Comment saved = commentRepository.save(comment);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);

    }

    // 댓글 수정

    // 댓글 삭제
}
