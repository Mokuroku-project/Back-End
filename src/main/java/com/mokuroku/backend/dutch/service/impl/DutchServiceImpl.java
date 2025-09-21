package com.mokuroku.backend.dutch.service.impl;

import com.mokuroku.backend.budgetbook.entity.BudgetbookEntity;
import com.mokuroku.backend.budgetbook.repository.BudgetbookRepository;
import com.mokuroku.backend.dutch.dto.DutchDTO;
import com.mokuroku.backend.dutch.dto.DutchToBudgetbookDTO;
import com.mokuroku.backend.dutch.service.DutchService;
import com.mokuroku.backend.exception.ErrorCode;
import com.mokuroku.backend.exception.impl.CustomException;
import com.mokuroku.backend.member.entity.Member;
import com.mokuroku.backend.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DutchServiceImpl implements DutchService {

    private final BudgetbookRepository budgetbookRepository;
    private final MemberRepository memberRepository;

    @Override
    public Map<String, Integer> dutch(DutchDTO dutchDTO) {
        Map<String, Integer> dutchResult = new HashMap<>();

        for (DutchDTO.DutchItem item : dutchDTO.getDutchList()) {
            int totalPrice = 0;

            if (item.getTotalPrice() != null) {
                totalPrice = item.getTotalPrice();
            } else if (item.getPrice() != null && item.getQuantity() != null) {
                totalPrice = item.getPrice() * item.getQuantity();
                item.setTotalPrice(totalPrice);
            } else {
                continue;
            }

            List<String> eaters = item.getEater();
            if (eaters == null || eaters.isEmpty()) continue;

            int dutchPrice = totalPrice / eaters.size();

            for (String person : eaters) {
                dutchResult.put(person, dutchResult.getOrDefault(person, 0) + dutchPrice);
            }
        }

        return dutchResult;
    }

    @Override
    public BudgetbookEntity dutchToBudgetbook(DutchToBudgetbookDTO dutchToBudgetbookDTO) {
        String email = "test@gmail.com";
        Member member = memberRepository.findById(email)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        Map<String, Integer> dutchResult = this.dutch(dutchToBudgetbookDTO.getDutchDTO());

        String selectPerson = dutchToBudgetbookDTO.getSelectPerson();
        Integer amount = dutchResult.get(selectPerson);

        BudgetbookEntity budgetbook = BudgetbookEntity.builder()
                .member(member)
                .type("지출")
                .amount(amount)
                .category(dutchToBudgetbookDTO.getBudgetbookDTO().getCategory())
                .memo(dutchToBudgetbookDTO.getBudgetbookDTO().getMemo())
                .date(dutchToBudgetbookDTO.getBudgetbookDTO().getDate())
                .build();

        return budgetbookRepository.save(budgetbook);
    }
}
