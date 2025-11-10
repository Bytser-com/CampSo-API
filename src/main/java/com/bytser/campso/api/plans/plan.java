package com.bytser.campso.api.plans;

import java.util.UUID;

import com.bytser.campso.api.campings.Camping;

import jakarta.persistence.*;

@Entity
@Table(name = "plans")
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional=false, fetch = FetchType.LAZY)
    @JoinColumn(name = "camping_id")
    private Camping camping;

    @Column(nullable = false, unique=false, updatable = true)
    private String name;

    @Column(columnDefinition = "TEXT", nullable = true, unique=false, updatable = true)
    private String description;

    @Column(nullable = false, unique=false, updatable = true)
    private double pricePerNight;

    @Column(nullable = false, unique=false, updatable = true)
    private int maxGuests;

    @Column(nullable = false, unique=false, updatable = true)
    private boolean available;

    @Column(nullable = true, unique=false, updatable = true)
    private boolean petsAllowed;

    protected Plan() {
        // JPA requirement
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }
    public String getIdString() {
        return id.toString();
    }

    public Camping getCamping() {
        return camping;
    }
    public void setCamping(Camping camping) {
        this.camping = camping;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }
    public void setPricePerNight(double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public int getMaxGuests() {
        return maxGuests;
    }
    public void setMaxGuests(int maxGuests) {
        this.maxGuests = maxGuests;
    }

    public boolean isAvailable() {
        return available;
    }
    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean isPetsAllowed() {
        return petsAllowed;
    }
    public void setPetsAllowed(boolean petsAllowed) {
        this.petsAllowed = petsAllowed;
    }
}
