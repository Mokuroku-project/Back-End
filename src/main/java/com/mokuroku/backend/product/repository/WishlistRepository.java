package com.mokuroku.backend.product.repository;

import com.mokuroku.backend.member.entity.Member;
import com.mokuroku.backend.product.entity.Wishlist;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

  List<Wishlist> findByEmail(Member member);

  @Query("""
          SELECT DISTINCT w
          FROM Wishlist w
          JOIN FETCH w.email m
          LEFT JOIN FETCH w.products p
          WHERE m.status = :status
      """)
  List<Wishlist> findAllByMemberStatusWithProducts(@Param("status") String status);

  Optional<Wishlist> findByWishlistIdAndEmail(long wishlistId, Member member);
}
