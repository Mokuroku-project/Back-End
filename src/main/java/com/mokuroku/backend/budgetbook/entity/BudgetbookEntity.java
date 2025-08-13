package com.mokuroku.backend.budgetbook.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mokuroku.backend.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.cglib.core.Local;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
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

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

}
