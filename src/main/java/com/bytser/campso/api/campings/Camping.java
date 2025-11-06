package com.bytser.campso.api.campings;

import jakarta.persistence.*;
import org.locationtech.jts.geom.Point;

import com.bytser.campso.api.facilities.Facility;
import com.bytser.campso.api.plans.Plan;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "campings")
public class Camping {

    @Id
    @Column(nullable = false, updatable = false, unique = true)
    private final String id = UUID.randomUUID().toString();

    @JdbcTypeCode(SqlTypes.GEOMETRY)
    @Column(columnDefinition = "geometry(Point,4326)", nullable = false, unique=true, updatable = false)
    private Point location;

    @Column(nullable = false, unique=false, updatable = true)
    private String name;

    @Column(nullable = true, unique=false, updatable = true)
    private String owner;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true, unique=false, updatable = true)
    private Rating rating;

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

    @Column(columnDefinition = "TEXT", nullable = true, unique=false, updatable = true)
    private String info;

    // Getters and Setters
    public String getId() {
        return id;
    }

    public UUID getUUID() {
        return UUID.fromString(id);
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public double getRating() {
        return rating.getValue();
    }

    public void setRating(double rating) {
        this.rating = Rating.fromValue(rating);
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

    public void setFacilities(List<Facility> facilities) {
        this.facilities = facilities;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
