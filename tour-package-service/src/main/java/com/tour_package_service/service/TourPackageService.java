package com.tour_package_service.service;

import com.tour_package_service.entity.TourPackage;
import com.tour_package_service.repository.TourPackageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TourPackageService {

    private final TourPackageRepository tourPackageRepository;

    public TourPackage createPackage(TourPackage request) {

        log.info("Creating tour package: {}", request.getPackageName());

        // Set relationship (VERY IMPORTANT)
        if (request.getDayNight() != null) {
            request.getDayNight().forEach(dn -> {
                dn.setTourPackage(request);
                dn.setCreatedAt(LocalDateTime.now());
            });
        }

        request.setCreatedAt(LocalDateTime.now());

        TourPackage saved = tourPackageRepository.save(request);

        log.info("Tour package created with id={}", saved.getPackageId());

        return saved;
    }

    // 🔹 GET BY ID
    public TourPackage getById(Long id) {
        log.info("Fetching package with id={}", id);

        return tourPackageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Package not found"));
    }

    // 🔹 GET ALL
    public List<TourPackage> getAll() {
        log.info("Fetching all packages");
        return tourPackageRepository.findAll();
    }

    // 🔹 GET ALL ACTIVE
    public List<TourPackage> getAllActive() {
        log.info("Fetching all active packages");
        return tourPackageRepository.findByActiveTrue();
    }

    // 🔹 UPDATE
    public TourPackage update(Long id, TourPackage request) {

        log.info("Updating package id={}", id);

        TourPackage existing = getById(id);

        existing.setPackageName(request.getPackageName());
        existing.setNoOfPersons(request.getNoOfPersons());
        existing.setDuration(request.getDuration());
        existing.setDestination(request.getDestination());
        existing.setActive(request.getActive());

        // 🔥 HANDLE CHILD UPDATE (IMPORTANT)
        existing.getDayNight().clear();

        if (request.getDayNight() != null) {
            request.getDayNight().forEach(dn -> {
                dn.setTourPackage(existing);
                dn.setCreatedAt(LocalDateTime.now());
            });

            existing.getDayNight().addAll(request.getDayNight());
        }

        return tourPackageRepository.save(existing);
    }

    // 🔹 ACTIVATE
    public void activate(Long id) {
        log.info("Activating package id={}", id);

        TourPackage pkg = getById(id);
        pkg.setActive(true);

        tourPackageRepository.save(pkg);
    }

    // 🔹 DEACTIVATE
    public void deactivate(Long id) {
        log.info("Deactivating package id={}", id);

        TourPackage pkg = getById(id);
        pkg.setActive(false);

        tourPackageRepository.save(pkg);
    }

    public TourPackage partialUpdate(Long id, TourPackage request) {

        log.info("Partially updating package id={}", id);

        TourPackage existing = getById(id);

        // ✅ Update only if not null
        if (request.getPackageName() != null)
            existing.setPackageName(request.getPackageName());

        if (request.getNoOfPersons() != null)
            existing.setNoOfPersons(request.getNoOfPersons());

        if (request.getDuration() != null)
            existing.setDuration(request.getDuration());

        if (request.getDestination() != null)
            existing.setDestination(request.getDestination());

        if (request.getActive() != null)
            existing.setActive(request.getActive());

        // 🔥 CHILD PARTIAL UPDATE (simple strategy)
        if (request.getDayNight() != null && !request.getDayNight().isEmpty()) {

            log.info("Updating dayNight details for package id={}", id);

            // Option 1: Replace completely
            existing.getDayNight().clear();

            request.getDayNight().forEach(dn -> {
                dn.setTourPackage(existing);
                dn.setCreatedAt(LocalDateTime.now());
            });

            existing.getDayNight().addAll(request.getDayNight());
        }

        return tourPackageRepository.save(existing);
    }

    public List<TourPackage> search(String destination, Boolean active) {

        log.info("Searching packages destination={}, active={}", destination, active);

        return tourPackageRepository.search(destination, active);
    }


}
