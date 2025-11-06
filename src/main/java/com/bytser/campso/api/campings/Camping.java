package com.bytser.campso.api.campings;

import jakarta.persistence.*;
import org.locationtech.jts.geom.Point;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;

@Entity
@Table(name = "campings")
public class Camping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JdbcTypeCode(SqlTypes.GEOMETRY)
    @Column(columnDefinition = "geometry(Point,4326)", nullable = false, unique=true, updatable = false)
    private Point loc;

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
    @CollectionTable(name = "campingPlans", joinColumns = @JoinColumn(name = "camping_id"))
    @Column(name = "campingPlans", nullable = false, unique=false, updatable = true)
    private List<String> campingPlans;      // List<CampingPlan>

    @Column(nullable = false, unique=false, updatable = true)
    private int totalSpaces;

    @ElementCollection
    @CollectionTable(name = "camping_facilities", joinColumns = @JoinColumn(name = "camping_id"))
    @Column(name = "facility", nullable = true, unique=false, updatable = true)
    private List<String> facilities;        // List<Facility>

    @Column(columnDefinition = "TEXT", nullable = true, unique=false, updatable = true)
    private String info;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Point getLoc() {
        return loc;
    }

    public void setLoc(Point loc) {
        this.loc = loc;
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

    public List<String> getPlans() {
        return campingPlans;
    }

    public void setPlans(List<String> campingPlans) {
        this.campingPlans = campingPlans;
    }

    public int getTotalSpaces() {
        return totalSpaces;
    }

    public void setTotalSpaces(int totalSpaces) {
        this.totalSpaces = totalSpaces;
    }

    public List<String> getFacilities() {
        return facilities;
    }

    public void setFacilities(List<String> facilities) {
        this.facilities = facilities;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
