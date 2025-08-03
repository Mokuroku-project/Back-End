package com.mokuroku.backend.budgetbook.entity;

import com.mokuroku.backend.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name="budgetbook")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BudgetbookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "budgetbook_id")
    private Long budgetbookId;

    @ManyToOne
    @JoinColumn(name = "email", referencedColumnName = "email")
    private Member member;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private Integer amount;

    @Column(nullable = false)
    private String category;

    @Column
    private String memo;

    @Column(name = "date", nullable = false)
    private LocalDateTime date = LocalDateTime.now();


}
