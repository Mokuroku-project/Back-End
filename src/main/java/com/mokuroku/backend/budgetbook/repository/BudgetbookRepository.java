package com.mokuroku.backend.budgetbook.repository;

import com.mokuroku.backend.budgetbook.entity.BudgetbookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BudgetbookRepository extends JpaRepository<BudgetbookEntity, Long> {

    Long budgetbookId(Long budgetbookId);
}
