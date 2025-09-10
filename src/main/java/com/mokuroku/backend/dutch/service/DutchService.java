package com.mokuroku.backend.dutch.service;

import com.mokuroku.backend.budgetbook.dto.BudgetbookDTO;
import com.mokuroku.backend.common.ResultDTO;
import com.mokuroku.backend.dutch.dto.DutchDTO;
import com.mokuroku.backend.dutch.dto.DutchToBudgetbookDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface DutchService {


    ResponseEntity<ResultDTO> dutchToBudgetbook(DutchToBudgetbookDTO dutchToBudgetbookDTO);

    ResponseEntity<ResultDTO> dutch(DutchDTO dutchDTO);


}