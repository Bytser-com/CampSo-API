package com.bytser.campso.api.activities;

import java.util.List;

import com.bytser.campso.api.facilities.Facility;
import com.bytser.campso.api.places.Place;

import jakarta.persistence.*;

@Entity
@Table(name = "activities")
public class Activity extends Place {
    @Column(nullable = true, unique=false, updatable = true)
    private String host;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique=false, updatable = true)
    private TargetAudience targetAudience;

    @Column(nullable = false, unique=false, updatable = true)
    private int totalSpaces;

    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Facility> facilities;

    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ActivitySchedule> schedules;

    protected Activity() {
        // Default constructor for JPA
    }

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

    public List<ActivitySchedule> getSchedules() {
        return schedules;
    }
    public void addSchedule(ActivitySchedule schedule) {
        this.schedules.add(schedule);
    }
    public void removeSchedule(ActivitySchedule schedule) {
        this.schedules.remove(schedule);
    }
}
