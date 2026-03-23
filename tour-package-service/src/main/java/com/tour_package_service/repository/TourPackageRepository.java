package com.tour_package_service.repository;

import com.tour_package_service.entity.TourPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TourPackageRepository extends JpaRepository<TourPackage, Long> {

    List<TourPackage> findByActiveTrue();

    @Query("SELECT p FROM TourPackage p WHERE (:destination IS NULL OR LOWER(p.destination) = LOWER(:destination)) AND (:active IS NULL OR p.active = :active)")
    List<TourPackage> search(@Param("destination") String destination, @Param("active") Boolean active
    );

}
