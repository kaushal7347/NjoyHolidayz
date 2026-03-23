package com.tour_package_service.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "package")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TourPackage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long packageId;

    private String packageName;
    private String noOfPersons;
    private String duration;
    private String destination;

    @JsonManagedReference
    @OneToMany(mappedBy = "tourPackage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DayNight> dayNight;

    private Boolean active;
    private LocalDateTime createdAt;

}
