package com.bytser.campso.api.reviews;

import java.util.UUID;

import jakarta.persistence.*;

import com.bytser.campso.api.places.Place;
import com.bytser.campso.api.users.User;

@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false,fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id")
    private Place place;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique=false, updatable = true)
    private Rating rating;

    @Column(columnDefinition = "TEXT", nullable = true, unique=false, updatable = true)
    private String info;

    @ManyToOne(optional=false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User owner;

    protected Review() {
        // JPA requirement
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + getIdString() +
                ", rating=" + rating +
                ", info='" + info + '\'' +
                '}';
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }
    public String getIdString() {
        return id != null ? id.toString() : null;
    }

    public Place getPlace() {
        return place;
    }
    public void setPlace(Place place) {
        this.place = place;
    }

    public Rating getRating() {
        return rating;
    }
    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public String getInfo() {
        return info;
    }
    public void setInfo(String info) {
        this.info = info;
    }

    public User getOwner() {
        return owner;
    }
    public void setOwner(User owner) {
        this.owner = owner;
    }
}
