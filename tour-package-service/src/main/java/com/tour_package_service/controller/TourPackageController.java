package com.tour_package_service.controller;


import com.tour_package_service.entity.TourPackage;
import com.tour_package_service.service.TourPackageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/packages")
@RequiredArgsConstructor
@Slf4j
public class TourPackageController {

    private final TourPackageService tourPackageService;

    @PostMapping("/tourPackage")
    public ResponseEntity<TourPackage> createPackage(
            @RequestBody TourPackage request) {

        log.info("API hit: Create Tour Package");

        TourPackage response = tourPackageService.createPackage(request);

        return ResponseEntity.ok(response);
    }

    // 🔹 GET BY ID
    @GetMapping("/tourPackage/{id}")
    public ResponseEntity<TourPackage> getById(@PathVariable Long id) {
        return ResponseEntity.ok(tourPackageService.getById(id));
    }

    // 🔹 GET ALL
    @GetMapping("/getAllTourPackage")
    public ResponseEntity<List<TourPackage>> getAll() {
        return ResponseEntity.ok(tourPackageService.getAll());
    }

    // 🔹 GET ALL ACTIVE
    @GetMapping("/getActiveTourPackage/active")
    public ResponseEntity<List<TourPackage>> getAllActive() {
        return ResponseEntity.ok(tourPackageService.getAllActive());
    }

    // 🔹 UPDATE
    @PutMapping("/tourPackage/{id}")
    public ResponseEntity<TourPackage> update(
            @PathVariable Long id,
            @RequestBody TourPackage request) {

        return ResponseEntity.ok(tourPackageService.update(id, request));
    }

    // 🔹 ACTIVATE
    @PutMapping("/tourPackage/{id}/activate")
    public ResponseEntity<String> activate(@PathVariable Long id) {
        tourPackageService.activate(id);
        return ResponseEntity.ok("Package activated");
    }

    // 🔹 DEACTIVATE
    @PutMapping("/tourPackage/{id}/deactivate")
    public ResponseEntity<String> deactivate(@PathVariable Long id) {
        tourPackageService.deactivate(id);
        return ResponseEntity.ok("Package deactivated");
    }

    @PatchMapping("/partialUpdate/{id}")
    public ResponseEntity<TourPackage> partialUpdate(
            @PathVariable Long id,
            @RequestBody TourPackage request) {

        return ResponseEntity.ok(tourPackageService.partialUpdate(id, request));
    }

    @GetMapping("/search")
    public ResponseEntity<List<TourPackage>> search(
            @RequestParam(required = false) String destination,
            @RequestParam(required = false) Boolean active) {

        return ResponseEntity.ok(tourPackageService.search(destination, active));
    }

}
