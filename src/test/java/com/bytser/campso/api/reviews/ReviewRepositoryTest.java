package com.bytser.campso.api.reviews;

import static org.assertj.core.api.Assertions.assertThat;

import com.bytser.campso.api.campings.Camping;
import com.bytser.campso.api.campings.TargetAudience;
import com.bytser.campso.api.support.TestDataFactory;
import com.bytser.campso.api.users.User;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest(properties = "spring.jpa.hibernate.ddl-auto=create-drop")
class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("findByPlaceId should return all reviews for the place")
    void shouldFindReviewsByPlaceId() {
        User owner = persistUser("1000");
        User reviewer = persistUser("1001");
        Camping camping = persistCamping(owner, "Blue Lake", 90.0);
        Camping otherCamping = persistCamping(owner, "Green Hill", 91.0);
        Review first = persistReview(reviewer, camping, Rating.FIVE_STAR, "Amazing");
        Review second = persistReview(reviewer, camping, Rating.FOUR_STAR, "Great");
        persistReview(reviewer, otherCamping, Rating.THREE_STAR, "Average");

        entityManager.flush();
        entityManager.clear();

        List<Review> result = reviewRepository.findByPlaceId(camping.getId());

        assertThat(result)
            .extracting(Review::getId)
            .containsExactlyInAnyOrder(first.getId(), second.getId());
    }

    @Test
    @DisplayName("findByOwnerId should return all reviews authored by the user")
    void shouldFindReviewsByOwnerId() {
        User owner = persistUser("1010");
        User reviewer = persistUser("1011");
        User otherReviewer = persistUser("1012");
        Camping camping = persistCamping(owner, "Sunrise Spot", 92.0);
        Review first = persistReview(reviewer, camping, Rating.FIVE_STAR, "Excellent");
        Review second = persistReview(reviewer, camping, Rating.TWO_STAR, "Needs work");
        persistReview(otherReviewer, camping, Rating.ONE_STAR, "Bad");

        entityManager.flush();
        entityManager.clear();

        List<Review> result = reviewRepository.findByOwnerId(reviewer.getId());

        assertThat(result)
            .extracting(Review::getId)
            .containsExactlyInAnyOrder(first.getId(), second.getId());
    }

    @Test
    @DisplayName("findByOwnerId should return an empty list when the user has not authored reviews")
    void shouldReturnEmptyListWhenReviewerUnknown() {
        User owner = persistUser("1020");
        User reviewer = persistUser("1021");
        Camping camping = persistCamping(owner, "Forest Retreat", 93.0);
        persistReview(reviewer, camping, Rating.THREE_STAR, "Good");

        entityManager.flush();
        entityManager.clear();

        List<Review> result = reviewRepository.findByOwnerId(UUID.randomUUID());

        assertThat(result).isEmpty();
    }

    private Camping persistCamping(User owner, String name, double longitude) {
        Camping camping = TestDataFactory.newCamping();
        camping.setName(name);
        camping.setOwner(owner);
        camping.setColorCode("#778899");
        camping.setLocation(TestDataFactory.createPoint(longitude, 16.0));
        camping.setInfo("Info for " + name);
        camping.setTargetAudience(TargetAudience.MIXED);
        camping.setTotalSpaces(50);
        return entityManager.persist(camping);
    }

    private Review persistReview(User owner, Camping camping, Rating rating, String info) {
        Review review = TestDataFactory.newReview();
        review.setOwner(owner);
        review.setPlace(camping);
        review.setRating(rating);
        review.setInfo(info);
        return entityManager.persist(review);
    }

    private User persistUser(String identifier) {
        return entityManager.persist(TestDataFactory.createUser(identifier));
    }
}
