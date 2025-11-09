package com.bytser.campso.api.campings;

import jakarta.persistence.*;

import com.bytser.campso.api.facilities.Facility;
import com.bytser.campso.api.places.Place;
import com.bytser.campso.api.plans.Plan;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;

@Entity
@Table(name = "campings")
public class Camping extends Place{

    @Column(nullable = true, unique=false, updatable = true)
    private String owner;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique=false, updatable = true)
    private TargetAudience targetAudience;

    @ElementCollection
    @CollectionTable(name = "camping_plans", joinColumns = @JoinColumn(name = "camping_id"))
    @Column(name = "plans", nullable = false, unique=false, updatable = true)
    private List<Plan> campingPlans;

    @Column(nullable = false, unique=false, updatable = true)
    private int totalSpaces;

    @ElementCollection
    @CollectionTable(name = "camping_facilities", joinColumns = @JoinColumn(name = "camping_id"))
    @Column(name = "facility", nullable = true, unique=false, updatable = true)
    private List<Facility> facilities;

    @OneToMany(mappedBy = "camping", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Plan> plans;

    // Getters and Setters
    public String getOwner() {
        return owner;
    }
    public void setOwner(String owner) {
        this.owner = owner;
    }

    public TargetAudience getTargetAudience() {
        return targetAudience;
    }
    public void setTargetAudience(TargetAudience targetAudience) {
        this.targetAudience = targetAudience;
    }

    public List<Plan> getPlans() {
        return campingPlans;
    }
    public void setPlans(List<Plan> campingPlans) {
        this.campingPlans = campingPlans;
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
