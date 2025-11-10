package com.bytser.campso.api.campings;

import jakarta.persistence.*;

import com.bytser.campso.api.facilities.Facility;
import com.bytser.campso.api.places.Place;
import com.bytser.campso.api.plans.Plan;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "campings")
public class Camping extends Place{

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique=false, updatable = true)
    private TargetAudience targetAudience;

    @OneToMany(mappedBy="camping", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private final List<Plan> campingPlans = new ArrayList<>();

    @Column(nullable = false, unique=false, updatable = true)
    private int totalSpaces;

    @OneToMany(mappedBy="hostPlace", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private final List<Facility> facilities = new ArrayList<>();

    protected Camping() {
        // Default constructor for JPA
    }

    @Override
    public String toString() {
        return "Camping{" +
                "id=" + getIdString() +
                ", name='" + getName() + '\'' +
                ", targetAudience=" + targetAudience +
                ", totalSpaces=" + totalSpaces +
                '}';
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
        if (plan == null) {
            return;
        }
        plan.setCamping(this);
        this.campingPlans.add(plan);
    }
    public void removePlan(Plan plan) {
        if (plan == null) {
            return;
        }
        plan.setCamping(null);
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
        if (facility == null) {
            return;
        }
        facility.setHostPlace(this);
        this.facilities.add(facility);
    }
    public void removeFacility(Facility facility) {
        if (facility == null) {
            return;
        }
        facility.setHostPlace(null);
        this.facilities.remove(facility);
    }
}
