package com.tour_package_service.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name = "day_night")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DayNight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dayNightId;

    private String flightSource;
    private String flightDestination;
    private String flightTime;
    private String flightPrice;

    private String hotelName;
    private String typeOfName;
    private String noOfRooms;
    private String pax;
    private String hotelPrice;

    private String mealPlan;
    private String transportFacility;

    private Boolean active;
    private LocalDateTime createdAt;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "package_id") // FK column
    private TourPackage tourPackage;

    private String dayName;// day1 | Friday
}
