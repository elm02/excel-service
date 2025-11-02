package com.example.excel_service.controller;

import com.example.excel_service.service.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/excel")
public class ExcelController {

    @Autowired
    private ExcelService excelService;

    @GetMapping("/find-smallest")
    public ResponseEntity<Map<String, String>> findSmallestNumber(
            @RequestParam String filePath,
            @RequestParam int n) {
        try {
            int result = excelService.findSmallestNumber(filePath, n);
            return ResponseEntity.ok(Map.of("answer", String.valueOf(result)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}