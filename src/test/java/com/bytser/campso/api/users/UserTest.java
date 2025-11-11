package com.bytser.campso.api.users;

import com.bytser.campso.api.places.Place;
import com.bytser.campso.api.reviews.Rating;
import com.bytser.campso.api.reviews.Review;
import com.bytser.campso.api.support.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = TestDataFactory.createUser("Main");
        user.setDateOfBirth(LocalDate.of(1990, 1, 1));
        user.setProfilePictureUrl("https://example.com/avatar.png");
        user.setRole(UserRole.ADMIN);
        user.setStatus(UserStatus.ACTIVE);
    }

    @Test
    @DisplayName("addReview links the review to the user")
    void addReviewShouldLinkBothSides() {
        Review review = new Review();
        review.setRating(Rating.FIVE_STAR);

        user.addReview(review);

        assertThat(user.getReviews()).containsExactly(review);
        assertThat(review.getOwner()).isEqualTo(user);
    }

    @Test
    @DisplayName("addReview ignores null reviews")
    void addReviewShouldIgnoreNull() {
        user.addReview(null);

        assertThat(user.getReviews()).isEmpty();
    }

    @Test
    @DisplayName("removeReview removes the review and clears owner")
    void removeReviewShouldUnlinkBothSides() {
        Review review = new Review();
        review.setRating(Rating.THREE_HALF_STAR);

        user.addReview(review);
        user.removeReview(review);

        assertThat(user.getReviews()).isEmpty();
        assertThat(review.getOwner()).isNull();
    }

    @Test
    @DisplayName("removeReview ignores null inputs")
    void removeReviewShouldIgnoreNull() {
        user.removeReview(null);

        assertThat(user.getReviews()).isEmpty();
    }

    @Test
    @DisplayName("addPlace links the place to the user")
    void addPlaceShouldLinkBothSides() {
        Place place = new TestPlace();
        place.setName("Lakeside");
        place.setLocation(TestDataFactory.createPoint(2.0, 46.0));
        place.setColorCode("#123123");

        user.addPlace(place);

        assertThat(user.getPlaces()).containsExactly(place);
        assertThat(place.getOwner()).isEqualTo(user);
    }

    @Test
    @DisplayName("addPlace ignores null places")
    void addPlaceShouldIgnoreNull() {
        user.addPlace(null);

        assertThat(user.getPlaces()).isEmpty();
    }

    @Test
    @DisplayName("removePlace removes the place and clears owner")
    void removePlaceShouldUnlinkBothSides() {
        Place place = new TestPlace();
        place.setName("Lakeside");
        place.setLocation(TestDataFactory.createPoint(2.0, 46.0));
        place.setColorCode("#123123");

        user.addPlace(place);
        user.removePlace(place);

        assertThat(user.getPlaces()).isEmpty();
        assertThat(place.getOwner()).isNull();
    }

    @Test
    @DisplayName("removePlace ignores null inputs")
    void removePlaceShouldIgnoreNull() {
        user.removePlace(null);

        assertThat(user.getPlaces()).isEmpty();
    }

    @Test
    @DisplayName("toString includes username, role, status, and avatar")
    void toStringShouldContainKeyFields() {
        String asString = user.toString();

        assertThat(asString)
                .contains(user.getUserName())
                .contains("ADMIN")
                .contains("ACTIVE")
                .contains("avatar.png");
    }

    private static class TestPlace extends Place {
    }
}
