package com.mokuroku.backend.member.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "member_role")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(com.example.mokuroku.backend.member.entity.MemberRoleId.class)  // 복합키 클래스 사용
public class MemberRole {

    @Id
    @Column(name = "member_id")
    private Long memberId;

    @Id
    @Column(name = "role_id")
    private Long roleId;

    // 연관관계 매핑은 필요에 따라 추가 가능
}
