package com.mokuroku.backend.product.repository;

import com.mokuroku.backend.product.entity.DailyPrice;
import com.mokuroku.backend.product.entity.Product;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyPriceRepository extends JpaRepository<DailyPrice, Long> {

  Optional<DailyPrice> findByProductAndDate(Product product, LocalDate date);
}
