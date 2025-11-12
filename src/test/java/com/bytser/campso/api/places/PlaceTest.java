package com.bytser.campso.api.places;

import com.bytser.campso.api.reviews.Rating;
import com.bytser.campso.api.reviews.Review;
import com.bytser.campso.api.support.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PlaceTest {

    private Place place;

    @BeforeEach
    @SuppressWarnings("unused") // compiler gives warning that function is never used, but function is used by Spring Boot annotations (@BeforeEach)
    void setUp() {
        place = new TestPlace();
        place.setName("Hidden Valley");
        place.setOwner(TestDataFactory.createUser("PlaceOwner"));
        place.setLocation(TestDataFactory.createPoint(10.0, 55.0));
        place.setColorCode("#336699");
        place.setInfo("Remote campsite with river access");
    }

    @Test
    @DisplayName("addReview links the review to the place bidirectionally")
    void addReviewShouldLinkBothSides() {
        Review review = new Review();
        review.setRating(Rating.FIVE_STAR);
        review.setOwner(TestDataFactory.createUser("ReviewWriter"));

        place.addReview(review);

        assertThat(place.getReviews()).containsExactly(review);
        assertThat(review.getPlace()).isEqualTo(place);
    }

    @Test
    @DisplayName("addReview ignores null reviews")
    void addReviewShouldIgnoreNull() {
        place.addReview(null);

        assertThat(place.getReviews()).isEmpty();
    }

    @Test
    @DisplayName("deleteReview removes the review and clears the place reference")
    void deleteReviewShouldUnlinkBothSides() {
        Review review = new Review();
        review.setRating(Rating.THREE_STAR);
        review.setOwner(TestDataFactory.createUser("ReviewWriter"));

        place.addReview(review);
        place.deleteReview(review);

        assertThat(place.getReviews()).isEmpty();
        assertThat(review.getPlace()).isNull();
    }

    @Test
    @DisplayName("deleteReview ignores null inputs")
    void deleteReviewShouldIgnoreNull() {
        place.deleteReview(null);

        assertThat(place.getReviews()).isEmpty();
    }

    @Test
    @DisplayName("deleteReview ignores reviews that were never attached")
    void deleteReviewShouldIgnoreMissingReview() {
        Review review = new Review();
        review.setRating(Rating.FIVE_STAR);
        review.setOwner(TestDataFactory.createUser("ReviewWriter"));
        place.addReview(review);

        Review stranger = new Review();
        stranger.setRating(Rating.ONE_STAR);
        stranger.setOwner(TestDataFactory.createUser("OtherWriter"));

        place.deleteReview(stranger);

        assertThat(place.getReviews()).containsExactly(review);
        assertThat(review.getPlace()).isEqualTo(place);
        assertThat(stranger.getPlace()).isNull();
    }

    @Test
    @DisplayName("toString includes the place name and color code")
    void toStringShouldContainKeyFields() {
        String asString = place.toString();

        assertThat(asString)
                .contains("Hidden Valley")
                .contains("#336699");
    }

    private static class TestPlace extends Place {
    }
}
