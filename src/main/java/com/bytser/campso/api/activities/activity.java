package com.bytser.campso.api.activities;

import java.util.List;

import com.bytser.campso.api.facilities.Facility;
import com.bytser.campso.api.places.Place;

import jakarta.persistence.*;

public class Activity extends Place {
    @Column(nullable = true, unique=false, updatable = true)
    private String host;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique=false, updatable = true)
    private TargetAudience targetAudience;

    @Column(nullable = false, unique=false, updatable = true)
    private int totalSpaces;

    @ElementCollection
    @CollectionTable(name = "camping_facilities", joinColumns = @JoinColumn(name = "activity_id"))
    @Column(name = "facility", nullable = true, unique=false, updatable = true)
    private List<Facility> facilities;

    // Getters and Setters
    public String getHost() {
        return host;
    }
    public void setHost(String host) {
        this.host = host;
    }

    public TargetAudience getTargetAudience() {
        return targetAudience;
    }
    public void setTargetAudience(TargetAudience targetAudience) {
        this.targetAudience = targetAudience;
    }

    public int getTotalSpaces() {
        return totalSpaces;
    }
    public void setTotalSpaces(int totalSpaces) {
        this.totalSpaces = totalSpaces;
    }

    public List<Facility> getFacilities() {
        return facilities;
    }
    public void addFacility(Facility facility) {
        this.facilities.add(facility);
    }
    public void removeFacility(Facility facility) {
        this.facilities.remove(facility);
    }
}
