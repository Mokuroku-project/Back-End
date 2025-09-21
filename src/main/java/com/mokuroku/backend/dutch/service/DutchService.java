package com.mokuroku.backend.dutch.service;

import com.mokuroku.backend.budgetbook.entity.BudgetbookEntity;
import com.mokuroku.backend.dutch.dto.DutchDTO;
import com.mokuroku.backend.dutch.dto.DutchToBudgetbookDTO;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface DutchService {


    BudgetbookEntity dutchToBudgetbook(DutchToBudgetbookDTO dutchToBudgetbookDTO);

    Map<String, Integer> dutch(DutchDTO dutchDTO);


}
