package com.mokuroku.backend.budgetbook.service;

import com.mokuroku.backend.budgetbook.dto.BudgetbookDTO;
import com.mokuroku.backend.budgetbook.dto.BudgetbookEditDTO;
import com.mokuroku.backend.budgetbook.entity.BudgetbookEntity;
import com.mokuroku.backend.common.ResultDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface BudgetbookService {

    BudgetbookEntity budgetbookRegist(BudgetbookDTO budgetbookDTO);

    void budgetbookDelete(Long budgetbookId);

    BudgetbookEntity budgetbookEdit(Long budgetbookId, BudgetbookEditDTO budgetbookEditDTO);

    BudgetbookEntity budgetbook(Long budgetbookId);

    Map<String, Object> budgetbookList(String email, String startDate, String endDate, String type);
}
