package com.mokuroku.backend.budgetbook.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class BudgetbookEditDTO {
        private String email;
        private String type; //지출, 수입
        private Integer amount; //금액
        private String category; //카테고리
        private String memo; //메모
        private LocalDate date;//해당일자

    }
