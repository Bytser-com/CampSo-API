package com.bytser.campso.api.reviews;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.bytser.campso.api.RepositoryTestSupport;
import com.bytser.campso.api.activities.Activity;
import com.bytser.campso.api.activities.ActivityRepository;
import com.bytser.campso.api.activities.TargetAudience;
import com.bytser.campso.api.users.CountryCode;
import com.bytser.campso.api.users.Language;
import com.bytser.campso.api.users.User;
import com.bytser.campso.api.users.UserRepository;

@DataJpaTest
class ReviewRepositoryTest extends RepositoryTestSupport {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void findByPlaceIdReturnsAllReviewsForPlace() {
        Activity activity = persistActivity("review-place-1", "Lava Springs");
        User reviewerOne = persistUser("reviewer-1");
        User reviewerTwo = persistUser("reviewer-2");

        Review reviewOne = buildReview(activity, reviewerOne, Rating.FIVE_STAR, "Amazing");
        Review reviewTwo = buildReview(activity, reviewerTwo, Rating.THREE_STAR, "Average");
        reviewRepository.save(reviewOne);
        reviewRepository.saveAndFlush(reviewTwo);

        List<Review> results = reviewRepository.findByPlaceId(activity.getId());

        assertThat(results)
                .hasSize(2)
                .extracting(Review::getOwner)
                .extracting(User::getId)
                .containsExactlyInAnyOrder(reviewerOne.getId(), reviewerTwo.getId());
    }

    @Test
    void findByOwnerIdReturnsAllReviewsAuthoredByUser() {
        Activity activity = persistActivity("review-place-2", "Crystal Cove");
        User reviewer = persistUser("reviewer-3");
        Review reviewOne = buildReview(activity, reviewer, Rating.FOUR_STAR, "Great");
        Review reviewTwo = buildReview(activity, reviewer, Rating.THREE_STAR, "Good");
        reviewRepository.save(reviewOne);
        reviewRepository.saveAndFlush(reviewTwo);

        List<Review> results = reviewRepository.findByOwnerId(reviewer.getId());

        assertThat(results)
                .hasSize(2)
                .extracting(Review::getRating)
                .containsExactlyInAnyOrder(Rating.FOUR_STAR, Rating.THREE_STAR);
    }

    @Test
    void deletingPlaceRemovesAssociatedReviews() {
        Activity activity = persistActivity("review-place-3", "Evergreen Park");
        User reviewer = persistUser("reviewer-4");
        Review review = buildReview(activity, reviewer, Rating.TWO_STAR, "Not great");
        reviewRepository.saveAndFlush(review);
        UUID reviewId = review.getId();

        activityRepository.delete(activity);
        activityRepository.flush();
        entityManager.clear();

        assertThat(reviewRepository.findById(reviewId)).isEmpty();
    }

    private Activity persistActivity(String suffix, String name) {
        User owner = persistUser("owner-" + suffix);
        Activity activity = new Activity();
        activity.setName(name);
        activity.setOwner(owner);
        activity.setLocation(point(5.6, 50.4));
        activity.setColorCode("#33AA77");
        activity.setHost("Host " + suffix);
        activity.setTargetAudience(TargetAudience.MIXED);
        activity.setTotalSpaces(35);
        return activityRepository.saveAndFlush(activity);
    }

    private Review buildReview(Activity place, User owner, Rating rating, String info) {
        Review review = new Review();
        review.setPlace(place);
        review.setOwner(owner);
        review.setRating(rating);
        review.setInfo(info);
        place.addReview(review);
        owner.getReviews().add(review);
        return review;
    }

    private User persistUser(String suffix) {
        User user = new User();
        user.setUserName("user-" + suffix);
        user.setFirstName("First" + suffix);
        user.setLastName("Last" + suffix);
        user.setEmail("user-" + suffix + "@example.com");
        user.setPasswordHash("hash-" + suffix);
        user.setCountryCode(CountryCode.NL);
        user.setLanguage(Language.EN);
        return userRepository.saveAndFlush(user);
    }
}
