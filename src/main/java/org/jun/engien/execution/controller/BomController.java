package org.jun.engien.execution.controller;

import lombok.RequiredArgsConstructor;
import org.jun.engien.execution.entity.Bom;
import org.jun.engien.execution.repository.BomRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@CrossOrigin
@RestController
@RequiredArgsConstructor
public class BomController {
    private final BomRepository bomRepository;

    @GetMapping("/api/bom/{bomId}")
    public ResponseEntity<?> bomIdHandle() {

        List<Bom> boms = bomRepository.findAll();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", 200);
        response.put("boms", boms);
        response.put("total", boms.size());

        return ResponseEntity.status(200).body(response);

    }
}
