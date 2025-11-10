package com.bytser.campso.api.reviews;

import com.bytser.campso.api.places.Place;
import com.bytser.campso.api.support.TestDataFactory;
import com.bytser.campso.api.users.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ReviewTest {

    private Review review;

    @BeforeEach
    void setUp() {
        review = new Review();
        review.setRating(Rating.FOUR);
        review.setInfo("Great stay with friendly hosts");
    }

    @Test
    void setOwnerShouldLinkReviewToUser() {
        User owner = TestDataFactory.createUser("ReviewOwner");

        review.setOwner(owner);

        assertThat(review.getOwner()).isEqualTo(owner);
    }

    @Test
    void setOwnerNullShouldUnlink() {
        User owner = TestDataFactory.createUser("ReviewOwner");
        review.setOwner(owner);

        review.setOwner(null);

        assertThat(review.getOwner()).isNull();
    }

    @Test
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
    void toStringShouldContainKeyFields() {
        String asString = review.toString();

        assertThat(asString)
                .contains("FOUR")
                .contains("Great stay");
    }

    private static class TestPlace extends Place {
    }
}
