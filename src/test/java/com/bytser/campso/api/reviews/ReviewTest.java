package com.bytser.campso.api.reviews;

import com.bytser.campso.api.places.Place;
import com.bytser.campso.api.support.TestDataFactory;
import com.bytser.campso.api.users.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ReviewTest {

    private Review review;

    @BeforeEach
    @SuppressWarnings("unused") // compiler gives warning that function is never used, but function is used by Spring Boot annotations (@BeforeEach)
    void setUp() {
        review = new Review();
        review.setRating(Rating.FOUR_STAR);
        review.setInfo("Great stay with friendly hosts");
    }

    @Test
    @DisplayName("setOwner assigns the review author")
    void setOwnerShouldLinkReviewToUser() {
        User owner = TestDataFactory.createUser("ReviewOwner");

        review.setOwner(owner);

        assertThat(review.getOwner()).isEqualTo(owner);
    }

    @Test
    @DisplayName("setOwner with null removes the author reference")
    void setOwnerNullShouldUnlink() {
        User owner = TestDataFactory.createUser("ReviewOwner");
        review.setOwner(owner);

        review.setOwner(null);

        assertThat(review.getOwner()).isNull();
    }

    @Test
    @DisplayName("setPlace associates the review with a place")
    void setPlaceShouldLinkReviewToPlace() {
        Place place = new TestPlace();
        place.setName("Seaside Camping");
        place.setOwner(TestDataFactory.createUser("PlaceOwner"));
        place.setLocation(TestDataFactory.createPoint(1.0, 2.0));
        place.setColorCode("#ABCDEF");

        review.setPlace(place);

        assertThat(review.getPlace()).isEqualTo(place);
    }

    @Test
    @DisplayName("setPlace with null removes the place association")
    void setPlaceNullShouldUnlink() {
        Place place = new TestPlace();
        place.setName("Seaside Camping");
        place.setOwner(TestDataFactory.createUser("PlaceOwner"));
        place.setLocation(TestDataFactory.createPoint(1.0, 2.0));
        place.setColorCode("#ABCDEF");

        review.setPlace(place);
        review.setPlace(null);

        assertThat(review.getPlace()).isNull();
    }

    @Test
    @DisplayName("toString includes rating and review summary")
    void toStringShouldContainKeyFields() {
        String asString = review.toString();

        assertThat(asString)
                .contains("FOUR")
                .contains("Great stay");
    }

    private static class TestPlace extends Place {
    }
}
