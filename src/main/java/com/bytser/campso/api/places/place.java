package com.bytser.campso.api.places;

import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import org.locationtech.jts.geom.*;

import com.bytser.campso.api.facilities.FacilityType;
import com.bytser.campso.api.reviews.Review;
import com.bytser.campso.api.users.User;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Place {

    @Id
    @Column(nullable = false, updatable = false, unique = true)
    private final UUID id = UUID.randomUUID();

    @Column(nullable = false, unique=false, updatable = true)
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User owner;

    // Use Point for POIs OR Polygon for areas. If you want both, store as Geometry.
    @JdbcTypeCode(SqlTypes.GEOMETRY)
    @Column(columnDefinition = "geometry(Geometry,4326)", nullable = false, updatable = true, unique = false)
    private Geometry location;

    @Column(nullable = false, updatable = true, unique = false)
    private String colorCode;  // eg "#4B9CD3"

    @Column(columnDefinition = "TEXT", nullable = true, unique=false, updatable = true)
    private String info;

    @ElementCollection
    @CollectionTable(name = "reviews", joinColumns = @JoinColumn(name = "place_id"))
    @Column(name = "reviews", nullable = true, unique=false, updatable = true)
    private List<Review> reviews;

    // Getters and Setters
    public UUID getId() {
        return id;
    }
    public String getIdString() {
        return id.toString();
    }

    public Geometry getLocation() {
        return location;
    }
    public void setLocation(Geometry location) {
        this.location = location;
    }

    public String getColorCode() {
        return colorCode;
    }
    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }
    public void setInfo(String info) {
        this.info = info;
    }
}
