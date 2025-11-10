package com.bytser.campso.api.places;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import org.locationtech.jts.geom.*;

import com.bytser.campso.api.reviews.Review;
import com.bytser.campso.api.users.User;

import jakarta.persistence.*;

@Entity
@Table(name = "places")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique=false, updatable = true)
    private String name;

    @ManyToOne(optional=false, fetch = FetchType.LAZY)
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

    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Review> reviews = new ArrayList<>();

    protected Place() {
        // Default constructor for JPA
    }

    @Override
    public String toString() {
        return "Place{" +
                "id=" + getIdString() +
                ", name='" + name + '\'' +
                ", location=" + location +
                ", colorCode='" + colorCode + '\'' +
                '}';
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }
    public String getIdString() {
        return id != null ? id.toString() : null;
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

    public User getOwner() {
        return owner;
    }
    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getInfo() {
        return info;
    }
    public void setInfo(String info) {
        this.info = info;
    }

    public List<Review> getReviews() {
        return reviews;
    }
    public void addReview(Review review) {
        if (review == null) {
            return;
        }
        review.setPlace(this);
        this.reviews.add(review);
    }
    public void deleteReview(Review review) {
        if (review == null) {
            return;
        }
        review.setPlace(null);
        this.reviews.remove(review);
    }
}
