package com.mokuroku.backend.notification.repository;

import com.mokuroku.backend.notification.entity.MemberPushToken;
import io.lettuce.core.dynamic.annotation.Param;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberPushTokenRepository extends JpaRepository<MemberPushToken, Long> {

  @Query("select t.token from MemberPushToken t where t.email.email = :email and t.revoked = false")
  List<String> findActiveTokensByEmail(@Param("email") String email);

  Optional<MemberPushToken> findByToken(String token);

  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query("""
        update MemberPushToken t
        set t.revoked = true, t.revokedDate = :revokedAt
        where t.token = :token and t.revoked = false
      """)
  int revokeByToken(String badToken, LocalDateTime revokedDate);

  default int revokeByTokenNow(String token) {
    return revokeByToken(token, LocalDateTime.now());
  }
}
