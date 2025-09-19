package com.mokuroku.backend.budgetbook.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
public class BudgetbookDTO {

    private Long budgetbookId; //Id값
    private String email; //이메일 값
    private String type; //지출, 수입
    private Integer amount; //금액
    private String category; //카테고리
    private String memo; //메모
    private LocalDate date;//해당일자

}
