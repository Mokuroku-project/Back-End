package com.mokuroku.backend.budgetbook.controller;

import com.mokuroku.backend.budgetbook.dto.BudgetbookDTO;
import com.mokuroku.backend.budgetbook.dto.BudgetbookEditDTO;
import com.mokuroku.backend.budgetbook.entity.BudgetbookEntity;
import com.mokuroku.backend.budgetbook.service.BudgetbookService;
import com.mokuroku.backend.common.ResultDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/budgetbook")
@RequiredArgsConstructor
public class BudgetbookController {

    private final BudgetbookService budgetbookService;

    // 가계부 등록
    @PostMapping("/")
    public ResponseEntity<ResultDTO<BudgetbookEntity>> budgetbook(@RequestBody BudgetbookDTO budgetbookDTO) {
        BudgetbookEntity result = budgetbookService.budgetbookRegist(budgetbookDTO);
        return ResponseEntity.ok(new ResultDTO<>("가계부 등록에 성공했습니다", result));
    }

    // 가계부 삭제
    @DeleteMapping("/{budgetbookId}")
    public ResponseEntity<ResultDTO<Void>> budgetbookDelete(@PathVariable Long budgetbookId) {
        budgetbookService.budgetbookDelete(budgetbookId);
        return ResponseEntity.ok(new ResultDTO<>("가계부 삭제에 성공했습니다", null));
    }

    // 가계부 수정
    @PutMapping("/{budgetbookId}")
    public ResponseEntity<ResultDTO<BudgetbookEntity>> budgetbookEdit(
            @PathVariable Long budgetbookId,
            @RequestBody BudgetbookEditDTO budgetbookDTO
    ) {
        BudgetbookEntity result = budgetbookService.budgetbookEdit(budgetbookId, budgetbookDTO);
        return ResponseEntity.ok(new ResultDTO<>("가계부 수정에 성공했습니다", result));
    }

    // 가계부 조회
    @GetMapping("/{budgetbookId}")
    public ResponseEntity<ResultDTO<BudgetbookEntity>> budgetbook(@PathVariable Long budgetbookId) {
        BudgetbookEntity result = budgetbookService.budgetbook(budgetbookId);
        return ResponseEntity.ok(new ResultDTO<>("가계부 조회에 성공했습니다", result));
    }

    // 리스트 조회
    @GetMapping("/list")
    public ResponseEntity<ResultDTO<Map<String, Object>>> budgetbookList(
            @RequestParam String email,
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam(required = false) String type
    ) {
        Map<String, Object> result = budgetbookService.budgetbookList(email, startDate, endDate, type);
        return ResponseEntity.ok(new ResultDTO<>("가계부 리스트 조회에 성공했습니다", result));
    }
}
