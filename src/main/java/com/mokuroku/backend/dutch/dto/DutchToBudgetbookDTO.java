package com.mokuroku.backend.dutch.dto;

import com.mokuroku.backend.budgetbook.dto.BudgetbookDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class DutchToBudgetbookDTO {

    private DutchDTO dutchDTO;
    private BudgetbookDTO budgetbookDTO;
    private String selectPerson;

}
