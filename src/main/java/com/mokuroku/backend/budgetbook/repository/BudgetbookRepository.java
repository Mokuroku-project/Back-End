package com.mokuroku.backend.budgetbook.repository;

import com.mokuroku.backend.budgetbook.entity.BudgetbookEntity;
import com.mokuroku.backend.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BudgetbookRepository extends JpaRepository<BudgetbookEntity, Long> {

    Long budgetbookId(Long budgetbookId);

    List<BudgetbookEntity> findByMemberAndDateBetweenAndType(Member member, LocalDate dateAfter, LocalDate dateBefore, String type);

    List<BudgetbookEntity> findByMemberAndDateBetween(Member member, LocalDate start, LocalDate end);
}
