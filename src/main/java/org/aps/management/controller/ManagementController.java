package org.aps.management.controller;

import lombok.RequiredArgsConstructor;
import org.aps.management.entity.Qna;
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

    // 게시글 전체 목록 조회
    @GetMapping
    public ResponseEntity<?> getAllQnas() {
        List<Qna> qnas = qnaRepository.findByDeletedFalse();
        return ResponseEntity.status(200).body(qnas);
    }

    // 특정 게시글 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<?> getQnaDetail(@PathVariable Integer id) {
        Qna qna = qnaRepository.findById(id).orElseThrow(() -> {
            return new ResponseStatusException(HttpStatus.NOT_FOUND);
        });

        if (Boolean.TRUE.equals(qna.getDeleted())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.status(200).body(qna);
    }

    // 게시글 등록
    @PostMapping
    public ResponseEntity<Qna> createQna(@RequestBody Qna qna) {
        Qna newQna = Qna.builder()
                .writerId(qna.getWriterId())
                .title(qna.getTitle())
                .content(qna.getContent())
                .wroteAt(LocalDateTime.now())
                .deleted(false)
                .build();

        Qna saved = qnaRepository.save(newQna);

        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
