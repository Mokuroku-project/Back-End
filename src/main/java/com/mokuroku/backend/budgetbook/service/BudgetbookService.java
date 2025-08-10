package com.mokuroku.backend.budgetbook.service;

import com.mokuroku.backend.budgetbook.dto.BudgetbookDTO;
import com.mokuroku.backend.budgetbook.dto.BudgetbookEditDTO;
import com.mokuroku.backend.common.ResultDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface BudgetbookService {

    ResponseEntity<ResultDTO> budgetbookRegist(BudgetbookDTO budgetbookDTO);

    ResponseEntity<ResultDTO> budgetbookDelete(Long budgetbookId);

    ResponseEntity<ResultDTO> budgetbookEdit(Long budgetbookId, BudgetbookEditDTO budgetbookEditDTO);
}
