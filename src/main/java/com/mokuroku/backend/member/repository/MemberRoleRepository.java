package com.mokuroku.backend.member.repository;

import com.mokuroku.backend.member.entity.MemberRole;
import com.mokuroku.backend.member.entity.MemberRoleId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRoleRepository extends JpaRepository<MemberRole, MemberRoleId> {
}
