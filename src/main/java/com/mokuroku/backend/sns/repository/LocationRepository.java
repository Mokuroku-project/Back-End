package com.mokuroku.backend.sns.repository;

import com.mokuroku.backend.sns.entity.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<LocationEntity, Long> {
    Optional<LocationEntity> findByLatitudeAndLongitude(Double latitude, Double longitude);
}
