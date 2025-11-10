package com.bytser.campso.api.users;

import com.bytser.campso.api.places.Place;
import com.bytser.campso.api.reviews.Rating;
import com.bytser.campso.api.reviews.Review;
import com.bytser.campso.api.support.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
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
    void addReviewShouldLinkBothSides() {
        Review review = new Review();
        review.setRating(Rating.FIVE);

        user.addReview(review);

        assertThat(user.getReviews()).containsExactly(review);
        assertThat(review.getOwner()).isEqualTo(user);
    }

    @Test
    void addReviewShouldIgnoreNull() {
        user.addReview(null);

        assertThat(user.getReviews()).isEmpty();
    }

    @Test
    void removeReviewShouldUnlinkBothSides() {
        Review review = new Review();
        review.setRating(Rating.THREE);

        user.addReview(review);
        user.removeReview(review);

        assertThat(user.getReviews()).isEmpty();
        assertThat(review.getOwner()).isNull();
    }

    @Test
    void removeReviewShouldIgnoreNull() {
        user.removeReview(null);

        assertThat(user.getReviews()).isEmpty();
    }

    @Test
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
    void addPlaceShouldIgnoreNull() {
        user.addPlace(null);

        assertThat(user.getPlaces()).isEmpty();
    }

    @Test
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
    void removePlaceShouldIgnoreNull() {
        user.removePlace(null);

        assertThat(user.getPlaces()).isEmpty();
    }

    @Test
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
