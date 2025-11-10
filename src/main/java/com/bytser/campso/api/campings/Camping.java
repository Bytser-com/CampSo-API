package com.bytser.campso.api.campings;

import jakarta.persistence.*;

import com.bytser.campso.api.facilities.Facility;
import com.bytser.campso.api.places.Place;
import com.bytser.campso.api.plans.Plan;

import java.util.List;

@Entity
@Table(name = "campings")
public class Camping extends Place{

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique=false, updatable = true)
    private TargetAudience targetAudience;

    @OneToMany(mappedBy="camping", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Plan> campingPlans;

    @Column(nullable = false, unique=false, updatable = true)
    private int totalSpaces;

    @OneToMany(mappedBy="camping", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Facility> facilities;

    protected Camping() {
        // Default constructor for JPA
    }

    // Getters and Setters
    public TargetAudience getTargetAudience() {
        return targetAudience;
    }
    public void setTargetAudience(TargetAudience targetAudience) {
        this.targetAudience = targetAudience;
    }

    public List<Plan> getPlans() {
        return campingPlans;
    }
    public void addPlan(Plan plan) {
        this.campingPlans.add(plan);
    }
    public void removePlan(Plan plan) {
        this.campingPlans.remove(plan);
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
