package com.mokuroku.backend.budgetbook.controller;


import com.mokuroku.backend.budgetbook.dto.BudgetbookDTO;
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

}
