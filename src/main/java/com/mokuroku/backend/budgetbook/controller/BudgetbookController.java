package com.mokuroku.backend.budgetbook.controller;


import com.mokuroku.backend.budgetbook.dto.BudgetbookDTO;
import com.mokuroku.backend.budgetbook.dto.BudgetbookEditDTO;
import com.mokuroku.backend.budgetbook.service.BudgetbookService;
import com.mokuroku.backend.common.ResultDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/budgetbook")
@RequiredArgsConstructor
public class BudgetbookController {

    private final BudgetbookService budgetbookService;

    @PostMapping("/")
    public ResponseEntity<ResultDTO> budgetbook(@RequestBody BudgetbookDTO budgetbookDTO){
        ResponseEntity<ResultDTO> result = budgetbookService.budgetbookRegist(budgetbookDTO);
        return result;
    }

    @DeleteMapping("/{budgetbookId}")
    public ResponseEntity<ResultDTO> budgetbookDelete(@RequestBody @PathVariable Long budgetbookId) {
        ResponseEntity<ResultDTO> result = budgetbookService.budgetbookDelete(budgetbookId);
        return result;
    }

    @PutMapping("/{budgetbookId}")
    public ResponseEntity<ResultDTO> budgetbookEdit(@PathVariable Long budgetbookId, @RequestBody BudgetbookEditDTO budgetbookDTO){
        ResponseEntity<ResultDTO> result = budgetbookService.budgetbookEdit(budgetbookId, budgetbookDTO);
        return result;
    }

    @GetMapping("/{budgetbookId}")
    public ResponseEntity<ResultDTO> budgetbook(@PathVariable Long budgetbookId){
        ResponseEntity<ResultDTO> result = budgetbookService.budgetbook(budgetbookId);
        return result;
    }

    @GetMapping("/list")
    public ResponseEntity<ResultDTO> budgetbookList(
        @RequestParam String email,
        @RequestParam String startDate,
        @RequestParam String endDate,
        @RequestParam String type
    ){
        ResponseEntity<ResultDTO> result = budgetbookService.budgetbookList(email, startDate, endDate, type);
        return result;
    }
}
