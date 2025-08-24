package com.mokuroku.backend.product.repository;

import com.mokuroku.backend.product.entity.DailyPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyPriceRepository extends JpaRepository<DailyPrice, Long> {

}
