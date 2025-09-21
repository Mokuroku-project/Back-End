package com.mokuroku.backend.dutch.controller;

import com.mokuroku.backend.budgetbook.entity.BudgetbookEntity;
import com.mokuroku.backend.common.ResultDTO;
import com.mokuroku.backend.dutch.dto.DutchDTO;
import com.mokuroku.backend.dutch.dto.DutchToBudgetbookDTO;
import com.mokuroku.backend.dutch.service.DutchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/dutch")
@RequiredArgsConstructor
public class DutchController {

    private final DutchService dutchService;

    @PostMapping("/")
    public ResponseEntity<ResultDTO<Map<String, Integer>>> dutch(@RequestBody DutchDTO dutchDTO) {
        Map<String, Integer> result = dutchService.dutch(dutchDTO);
        return ResponseEntity.ok(new ResultDTO<>("더치페이에 성공하였습니다", result));
    }

    @PostMapping("/budgetbook")
    public ResponseEntity<ResultDTO<BudgetbookEntity>> dutchToBudgetbook(@RequestBody DutchToBudgetbookDTO dto) {
        BudgetbookEntity budgetbook = dutchService.dutchToBudgetbook(dto);
        return ResponseEntity.ok(new ResultDTO<>("더치페이 가계부 추가에 성공했습니다", budgetbook));
    }
}
