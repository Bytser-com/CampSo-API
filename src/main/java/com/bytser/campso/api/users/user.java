package com.bytser.campso.api.users;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.bytser.campso.api.places.Place;

import jakarta.persistence.*;

import com.bytser.campso.api.reviews.Review;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.Email;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique=true, updatable = true)
    private String userName;

    @Column(nullable = false, unique=false, updatable = true)
    private String firstName;

    @Column(nullable = false, unique=false, updatable = true)
    private String lastName;

    @Column(nullable = false, unique=true, updatable = true)
    @Email
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role = UserRole.USER;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status = UserStatus.UNVERIFIED;

    @Column(nullable = false, updatable = false, unique = false)
    private final LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = true, updatable = true, unique = false)
    private LocalDate dateOfBirth;

    @Column(nullable = true, updatable = true, unique = false)
    private String profilePictureUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true, updatable = true, unique = false)
    private Language language = Language.EN;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true, updatable = true, unique = false)
    private CountryCode countryCode;

    @Column(nullable = false, unique=false, updatable = true)
    @JsonIgnore
    private String passwordHash;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Place> places;

    protected User() {
        // JPA requirement
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }
    public String getIdString() {
        return id.toString();
    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public UserRole getRole() {
        return role;
    }
    public void setRole(UserRole role) {
        this.role = role;
    }

    public UserStatus getStatus() {
        return status;
    }
    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }
    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public Language getLanguage() {
        return language;
    }
    public void setLanguage(Language language) {
        this.language = language;
    }

    public CountryCode getCountryCode() {
        return countryCode;
    }
    public void setCountryCode(CountryCode countryCode) {
        this.countryCode = countryCode;
    }

    public String getPasswordHash() {
        return passwordHash;
    }
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public List<Review> getReviews() {
        return reviews;
    }
    public void addReview(Review review) {
        this.reviews.add(review);
    }
    public void removeReview(Review review) {
        this.reviews.remove(review);
    }

    public List<Place> getPlaces() {
        return places;
    }
    public void addPlace(Place place) {
        this.places.add(place);
    }
    public void removePlace(Place place) {
        this.places.remove(place);
    }

}
