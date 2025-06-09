package org.aps.engine.scenario.bop.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.aps.engine.scenario.bop.service.BomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/scenarios/bop")
@RequiredArgsConstructor
public class BopController {

    private final BomService bomService;

    // 🔽 엑셀 업로드: 파일을 받아 DB에 저장
    @PostMapping("/upload")
    public ResponseEntity<String> uploadBomExcel(@RequestParam("file") MultipartFile file) throws IOException {
        bomService.excelHandle(file);
        return ResponseEntity.ok("엑셀 업로드 및 저장 완료");
    }

    // 🔽 엑셀 다운로드: DB 데이터를 엑셀로 응답
    @GetMapping("/download")
    public void downloadBomExcel(HttpServletResponse response) throws IOException {
        bomService.exportBomExcel(response);
    }
}
