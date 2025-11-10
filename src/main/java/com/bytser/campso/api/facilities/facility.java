package com.bytser.campso.api.facilities;

import com.bytser.campso.api.places.Place;

import jakarta.persistence.*;

@Entity
@Table(name = "facilities")
public class Facility extends Place {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique=false, updatable = true)
    private FacilityType facilityType;

    @ManyToOne(optional=false, fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id")
    private Place place;

    protected Facility() {
        // Default constructor for JPA
    }

    // Getters and Setters
    public FacilityType getFacilityType() {
        return facilityType;
    }
    public void setFacilityType(FacilityType facilityType) {
        this.facilityType = facilityType;
    }

    public Place getPlace() {
        return place;
    }
    public void setPlace(Place place) {
        this.place = place;
    }
}
