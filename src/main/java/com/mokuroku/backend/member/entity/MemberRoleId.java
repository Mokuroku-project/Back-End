package com.mokuroku.backend.member.entity;

import java.io.Serializable;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MemberRoleId implements Serializable {
    private Long memberId;
    private Long roleId;
}
