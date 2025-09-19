package com.mokuroku.backend.product.repository;

import com.mokuroku.backend.product.entity.Product;
import com.mokuroku.backend.product.entity.Wishlist;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

  Optional<Product> findByWishlist(Wishlist wishlist);
}
