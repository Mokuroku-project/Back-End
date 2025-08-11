package com.mokuroku.backend.budgetbook.service.impl;

import com.mokuroku.backend.budgetbook.dto.BudgetbookDTO;
import com.mokuroku.backend.budgetbook.dto.BudgetbookEditDTO;
import com.mokuroku.backend.budgetbook.entity.BudgetbookEntity;
import com.mokuroku.backend.budgetbook.repository.BudgetbookRepository;
import com.mokuroku.backend.budgetbook.service.BudgetbookService;
import com.mokuroku.backend.common.ResultDTO;
import com.mokuroku.backend.exception.ErrorCode;
import com.mokuroku.backend.exception.impl.CustomException;
import com.mokuroku.backend.member.entity.Member;
import com.mokuroku.backend.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                .date(budgetbookDTO.getDate())
                .build();

        budgetbookRepository.save(budgetbook);


        return ResponseEntity.ok(new ResultDTO<>("가계부 작성에 성공했습니다",  budgetbook));
    }




    //가계부 삭제
    @Override
    public ResponseEntity<ResultDTO> budgetbookDelete(Long budgetbookId){
        String email = "test@gmail.com";
        Member member = memberRepository.findById(email)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        BudgetbookEntity budgetbook = budgetbookRepository.findById(budgetbookId)
                        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_BUDGETBOOK));

        if (!budgetbook.getMember().getEmail().equals(email)) {
            throw new CustomException(ErrorCode.NOT_FOUND_BUDGETBOOK);
        }

        budgetbookRepository.deleteById(budgetbookId);

        return ResponseEntity.ok(new ResultDTO<>("가계부 삭제에 성공했습니다",""));
    }


    //가계부 수정
    @Override
    @Transactional
    public ResponseEntity<ResultDTO> budgetbookEdit(Long budgetbookId, BudgetbookEditDTO budgetbookEditDTO){
        String email = "test@gmail.com";
        Member member = memberRepository.findById(email)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        BudgetbookEntity budgetbook = budgetbookRepository.findById(budgetbookId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_BUDGETBOOK));

        budgetbook.setType(budgetbookEditDTO.getType());
        budgetbook.setAmount(budgetbookEditDTO.getAmount());
        budgetbook.setCategory(budgetbookEditDTO.getCategory());
        budgetbook.setMemo(budgetbookEditDTO.getMemo());
        budgetbook.setDate(budgetbookEditDTO.getDate());

        budgetbookRepository.save(budgetbook);

        return ResponseEntity.ok(new ResultDTO<>("가계부 수정에 성공했습니다",budgetbook));
    }


    //가계부 조회
    @Override
    public ResponseEntity<ResultDTO> budgetbook(Long budgetbookId){
        String email = "test@gmail.com";
        Member member = memberRepository.findById(email)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        BudgetbookEntity budgetbook = budgetbookRepository.findById(budgetbookId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_BUDGETBOOK));

        if(!budgetbook.getMember().getEmail().equals(email) || !budgetbook.getBudgetbookId().equals(budgetbookId)) {
            throw new CustomException(ErrorCode.NOT_FOUND_BUDGETBOOK);
        }
        budgetbookRepository.findById(budgetbookId);

        return ResponseEntity.ok(new ResultDTO<>("가계부 조회에 성공했습니다", budgetbook));

    }


    //가계부 리스트 조회
    @Override
    public ResponseEntity<ResultDTO> budgetbookList(String email, String startDate, String endDate, String type) {

        Member member = memberRepository.findById(email)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        LocalDate start = LocalDate.parse(startDate);
        LocalDate end   = LocalDate.parse(endDate);

        List<BudgetbookEntity> budgetbookList;
        if (type == null || type.isEmpty()) {
            budgetbookList = budgetbookRepository.findByMemberAndDateBetween(member, start, end);
        } else {
            budgetbookList = budgetbookRepository.findByMemberAndDateBetweenAndType(member, start, end, type);
        }

        int totalIncome = budgetbookList.stream()
                .filter(b -> "수입".equals(b.getType()))
                .mapToInt(BudgetbookEntity::getAmount)
                .sum();

        int totalExpense = budgetbookList.stream()
                .filter(b -> "지출".equals(b.getType()))
                .mapToInt(BudgetbookEntity::getAmount)
                .sum();

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("totalIncome", totalIncome);
        resultMap.put("totalExpense", totalExpense);
        resultMap.put("items", budgetbookList);

        return ResponseEntity.ok(new ResultDTO<>("가계부 리스트 조회에 성공했습니다", resultMap));
    }



}
