package com.mokuroku.backend.dutch.controller;

import com.mokuroku.backend.common.ResultDTO;
import com.mokuroku.backend.dutch.dto.DutchDTO;
import com.mokuroku.backend.dutch.dto.DutchToBudgetbookDTO;
import com.mokuroku.backend.dutch.service.DutchService;
import com.mokuroku.backend.dutch.service.impl.DutchServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/dutch")
@RequiredArgsConstructor
public class DutchController {

    private final DutchService dutchService;

    @PostMapping("/")
    public ResponseEntity<ResultDTO> dutch(@RequestBody DutchDTO dutchDTO){
        ResponseEntity<ResultDTO> result = dutchService.dutch(dutchDTO);
        return result;
    }

    @PostMapping("/budgetbook")
    public ResponseEntity<ResultDTO> dutchToBudgetbook(@RequestBody DutchToBudgetbookDTO dutchToBudgetbookDTO){
        ResponseEntity<ResultDTO> result = dutchService.dutchToBudgetbook(dutchToBudgetbookDTO);

        return result;

    }

}