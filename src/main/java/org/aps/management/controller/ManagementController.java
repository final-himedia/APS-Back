package org.aps.management.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aps.common.entity.User;
import org.aps.common.repository.UserRepository;
import org.aps.management.entity.Comment;
import org.aps.management.entity.Qna;
import org.aps.management.repository.CommentRepository;
import org.aps.management.repository.QnaRepository;
import org.aps.management.response.CommentResponse;
import org.aps.management.response.QnaResponse;
import org.aps.management.response.UserListResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/management")
@RequiredArgsConstructor
public class ManagementController {
    private final QnaRepository qnaRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    // 사용자 전체 목록 조회
    @GetMapping("/user")
    public ResponseEntity<?> allUsers(HttpServletRequest request) {
        User loginUser = (User) request.getAttribute("user");

        // 관리자가 아니면 접근 제한
        if (!"Admin".equalsIgnoreCase(loginUser.getRole())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "접근 권한이 없습니다.");
        }

        // 전체 사용자 조회
        List<User> users = userRepository.findAll();

        // 사용자 리스트를 Map 형태로 변환
        List<Map<String, Object>> results = new ArrayList<>();
        for (User user : users) {
            Map<String, Object> userMap = new LinkedHashMap<>();
            userMap.put("id", user.getId());
            userMap.put("email", user.getEmail());
            userMap.put("name", user.getName());
            userMap.put("role", user.getRole());
            results.add(userMap);
        }

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", 200);
        response.put("users", users);
        response.put("total", users.size());

        return ResponseEntity.status(200).body(response);
    }

    // 사용자 정보 수정
    @PutMapping("/user/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id,
                                        @RequestBody UserListResponse request,
                                        HttpServletRequest httpRequest) {
        User loginUser = (User) httpRequest.getAttribute("user");

        // 관리자가 아니면 접근 제한
        if (!"Admin".equalsIgnoreCase(loginUser.getRole())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "접근 권한이 없습니다.");
        }

        // 특정 사용자 조회
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 사용자를 찾을 수 없습니다."));

        // 요청 값으로 사용자 정보 수정
        if (request.getName() != null) {
            user.setName(request.getName());
        }
        if (request.getRole() != null) {
            user.setRole(request.getRole());
        }

        User updated = userRepository.save(user);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("id", updated.getId());
        response.put("email", updated.getEmail());
        response.put("name", updated.getName());
        response.put("role", updated.getRole());

        Map<String, Object> update = new LinkedHashMap<>();
        update.put("status", 200);
        update.put("updated", updated);
        update.put("total", update.size());

        return ResponseEntity.status(200).body(update);
    }

    // 사용자 정보 삭제
    @DeleteMapping("/user/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id, HttpServletRequest request) {
        User loginUser = (User) request.getAttribute("user");

        // 관리자가 아니면 접근 제한
        if (!"Admin".equalsIgnoreCase(loginUser.getRole())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "접근 권한이 없습니다.");
        }

        // 특정 사용자 조회
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 사용자를 찾을 수 없습니다."));

        userRepository.delete(user);

        return ResponseEntity.status(204).build();
    }

    // 전체 게시글 목록 조회
    @GetMapping("/qna/list")
    public ResponseEntity<List<QnaResponse>> allQnas() {
        List<Qna> qnas = qnaRepository.findByDeletedFalse();
        List<QnaResponse> results = new ArrayList<>();

        for (Qna qna : qnas) {
            User writer = userRepository.findById(qna.getWriterId()).orElse(null);

            QnaResponse response = QnaResponse.builder()
                    .id(qna.getId())
                    .writerId(qna.getWriterId())
                    .title(qna.getTitle())
                    .content(qna.getContent())
                    .wroteAt(qna.getWroteAt())
                    .email(writer != null ? writer.getEmail() : "알 수 없는 사용자")
                    .name(writer != null ? writer.getName() : "이름 없음")
                    .build();

            results.add(response);
        }

        return ResponseEntity.status(200).body(results);
    }

    // 게시글 상세 조회
    @GetMapping("/qna/detail/{id}")
    public ResponseEntity<QnaResponse> detailQna(@PathVariable Integer id) {
        Qna qna = qnaRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 게시글이 존재하지 않습니다."));

        User writer = userRepository.findById(qna.getWriterId()).orElse(null);

        QnaResponse response = QnaResponse.builder()
                .id(qna.getId())
                .writerId(qna.getWriterId())
                .title(qna.getTitle())
                .content(qna.getContent())
                .wroteAt(qna.getWroteAt())
                .email(writer != null ? writer.getEmail() : "알 수 없는 사용자")
                .name(writer != null ? writer.getName() : "이름 없음")
                .build();

        return ResponseEntity.status(200).body(response);
    }

    // 게시글 등록
    @PostMapping("/qna")
    public ResponseEntity<Qna> createQna(@RequestBody Qna request, HttpServletRequest httpRequest) {
        User loginUser = (User) httpRequest.getAttribute("user");

        if (loginUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }

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
    @PutMapping("/qna/{id}")
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
    @DeleteMapping("/qna/{id}")
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

    // 제목으로 게시글 검색
    @GetMapping("/qna/search/title")
    public ResponseEntity<List<QnaResponse>> searchByTitle(@RequestParam String title) {
        List<Qna> results = qnaRepository.findByTitleContainingAndDeletedFalse(title);
        if (results.isEmpty()) return ResponseEntity.noContent().build();

        List<QnaResponse> responseList = new ArrayList<>();
        for (Qna qna : results) {
            User writer = userRepository.findById(qna.getWriterId()).orElse(null);
            responseList.add(QnaResponse.builder()
                    .id(qna.getId())
                    .writerId(qna.getWriterId())
                    .title(qna.getTitle())
                    .content(qna.getContent())
                    .wroteAt(qna.getWroteAt())
                    .email(writer != null ? writer.getEmail() : "알 수 없는 사용자")
                    .name(writer != null ? writer.getName() : "이름 없음")
                    .build());
        }

        return ResponseEntity.status(200).body(responseList);
    }

    // 작성자로 게시글 검색
    @GetMapping("/qna/search/writer")
    public ResponseEntity<List<QnaResponse>> searchByWriterName(@RequestParam String name) {
        User writer = userRepository.findByName(name)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 사용자를 찾을 수 없습니다."));

        List<Qna> results = qnaRepository.findByWriterIdAndDeletedFalse(writer.getId());
        if (results.isEmpty()) return ResponseEntity.noContent().build();

        List<QnaResponse> responseList = new ArrayList<>();
        for (Qna qna : results) {
            responseList.add(QnaResponse.builder()
                    .id(qna.getId())
                    .writerId(qna.getWriterId())
                    .title(qna.getTitle())
                    .content(qna.getContent())
                    .wroteAt(qna.getWroteAt())
                    .email(writer.getEmail())
                    .name(writer.getName())
                    .build());
        }

        return ResponseEntity.ok(responseList);
    }

    // 댓글 목록 조회
    @GetMapping("/qna/{qnaId}/comment/list")
    public ResponseEntity<List<CommentResponse>> getComments(@PathVariable Integer qnaId) {
        List<Comment> comments = commentRepository.findByQnaIdAndDeletedFalse(qnaId);
        List<CommentResponse> results = new ArrayList<>();

        for (Comment comment : comments) {
            User writer = userRepository.findById(comment.getWriterId()).orElse(null);

            CommentResponse response = CommentResponse.builder()
                    .id(comment.getId())
                    .content(comment.getContent())
                    .wroteAt(comment.getWroteAt())
                    .email(writer != null ? writer.getEmail() : "알 수 없는 사용자")
                    .name(writer != null ? writer.getName() : "이름 없음")
                    .build();

            results.add(response);
        }

        return ResponseEntity.status(200).body(results);
    }


    // 댓글 등록
    @PostMapping("/qna/{qnaId}/comment")
    public ResponseEntity<CommentResponse> createComment(@PathVariable Integer qnaId,
                                                         @RequestBody Comment request,
                                                         HttpServletRequest httpRequest) {
        Qna qna = qnaRepository.findByIdAndDeletedFalse(qnaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 게시글입니다."));

        User loginUser = (User) httpRequest.getAttribute("user");
        if (loginUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }

        Comment comment = Comment.builder()
                .qnaId(qnaId)
                .writerId(loginUser.getId())
                .content(request.getContent())
                .wroteAt(LocalDateTime.now())
                .deleted(false)
                .build();

        Comment saved = commentRepository.save(comment);

        CommentResponse response = CommentResponse.builder()
                .id(saved.getId())
                .content(saved.getContent())
                .wroteAt(saved.getWroteAt())
                .email(loginUser.getEmail())
                .name(loginUser.getName())
                .build();

        return ResponseEntity.status(201).body(response);
    }

    // 댓글 수정
    @PutMapping("/qna/{qnaId}/comment/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(@PathVariable Integer qnaId,
                                                 @PathVariable Integer commentId,
                                                 @RequestBody Comment request,
                                                 HttpServletRequest httpRequest) {
        User loginUser = (User) httpRequest.getAttribute("user");

        if (loginUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }

        Comment comment = commentRepository.findByIdAndQnaIdAndDeletedFalse(commentId, qnaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다."));

        if (!loginUser.getId().equals(comment.getWriterId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "수정 권한이 없습니다.");
        }

        comment.setContent(request.getContent());
        Comment updated = commentRepository.save(comment);

        CommentResponse response = CommentResponse.builder()
                .id(updated.getId())
                .content(updated.getContent())
                .wroteAt(updated.getWroteAt())
                .email(loginUser.getEmail())
                .name(loginUser.getName())
                .build();

        return ResponseEntity.status(200).body(response);
    }

    // 댓글 삭제
    @DeleteMapping("/qna/{qnaId}/comment/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Integer qnaId,
                                              @PathVariable Integer commentId,
                                              HttpServletRequest httpRequest) {
        User loginUser = (User) httpRequest.getAttribute("user");

        if (loginUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }

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
