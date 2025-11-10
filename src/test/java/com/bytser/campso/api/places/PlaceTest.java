package com.bytser.campso.api.places;

import com.bytser.campso.api.reviews.Rating;
import com.bytser.campso.api.reviews.Review;
import com.bytser.campso.api.support.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PlaceTest {

    private Place place;

    @BeforeEach
    void setUp() {
        place = new TestPlace();
        place.setName("Hidden Valley");
        place.setOwner(TestDataFactory.createUser("PlaceOwner"));
        place.setLocation(TestDataFactory.createPoint(10.0, 55.0));
        place.setColorCode("#336699");
        place.setInfo("Remote campsite with river access");
    }

    @Test
    void addReviewShouldLinkBothSides() {
        Review review = new Review();
        review.setRating(Rating.FIVE);
        review.setOwner(TestDataFactory.createUser("ReviewWriter"));

        place.addReview(review);

        assertThat(place.getReviews()).containsExactly(review);
        assertThat(review.getPlace()).isEqualTo(place);
    }

    @Test
    void addReviewShouldIgnoreNull() {
        place.addReview(null);

        assertThat(place.getReviews()).isEmpty();
    }

    @Test
    void deleteReviewShouldUnlinkBothSides() {
        Review review = new Review();
        review.setRating(Rating.THREE);
        review.setOwner(TestDataFactory.createUser("ReviewWriter"));

        place.addReview(review);
        place.deleteReview(review);

        assertThat(place.getReviews()).isEmpty();
        assertThat(review.getPlace()).isNull();
    }

    @Test
    void deleteReviewShouldIgnoreNull() {
        place.deleteReview(null);

        assertThat(place.getReviews()).isEmpty();
    }

    @Test
    void toStringShouldContainKeyFields() {
        String asString = place.toString();

        assertThat(asString)
                .contains("Hidden Valley")
                .contains("#336699");
    }

    private static class TestPlace extends Place {
    }
}
