package com.mokuroku.backend.budgetbook.service.impl;

import com.mokuroku.backend.budgetbook.dto.BudgetbookDTO;
import com.mokuroku.backend.budgetbook.entity.BudgetbookEntity;
import com.mokuroku.backend.budgetbook.repository.BudgetbookRepository;
import com.mokuroku.backend.budgetbook.service.BudgetbookService;
import com.mokuroku.backend.common.ResultDTO;
import com.mokuroku.backend.exception.ErrorCode;
import com.mokuroku.backend.exception.impl.CustomException;
import com.mokuroku.backend.member.entity.Member;
import com.mokuroku.backend.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BudgetbookServiceImpl implements BudgetbookService {

    private final MemberRepository memberRepository;
    private final BudgetbookRepository budgetbookRepository;


    //가계부 생성
    @Override
    public ResponseEntity<ResultDTO> budgetbookRegist(BudgetbookDTO budgetbookDTO){
        String email = "test@gmail.com";
        Member member = memberRepository.findById(email)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        BudgetbookEntity budgetbook = BudgetbookEntity.builder()
                .member(member)
                .type(budgetbookDTO.getType())
                .amount(budgetbookDTO.getAmount())
                .category(budgetbookDTO.getCategory())
                .memo(budgetbookDTO.getMemo())
                .date(LocalDateTime.now())
                .build();

        budgetbookRepository.save(budgetbook);


        return ResponseEntity.ok(new ResultDTO<>("가계부 작성에 성공했습니다",  budgetbook));
    };


    //가계부 삭제
    @Override
    public ResponseEntity<ResultDTO> budgetbookDelete(BudgetbookDTO budgetbookDTO){
        String email = "test@gmail.com";
        Member member = memberRepository.findById(email)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        budgetbookRepository.deleteById(budgetbookDTO.getBudgetbookId());

        return ResponseEntity.ok(new ResultDTO<>("가계부 삭제에 성공했습니다",""));
    }

}
