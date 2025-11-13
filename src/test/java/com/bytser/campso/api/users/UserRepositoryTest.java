package com.bytser.campso.api.users;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;

import com.bytser.campso.api.RepositoryTestSupport;
import com.bytser.campso.api.activities.Activity;
import com.bytser.campso.api.activities.TargetAudience;
import com.bytser.campso.api.places.PlaceRepository;
import com.bytser.campso.api.reviews.Rating;
import com.bytser.campso.api.reviews.Review;
import com.bytser.campso.api.reviews.ReviewRepository;

@DataJpaTest
class UserRepositoryTest extends RepositoryTestSupport {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void findByEmailReturnsPersistedUser() {
        User saved = userRepository.saveAndFlush(buildUser("alpha"));

        assertThat(userRepository.findByEmail(saved.getEmail()))
                .isPresent()
                .get()
                .extracting(User::getId)
                .isEqualTo(saved.getId());
    }

    @Test
    void findByUserNameReturnsPersistedUser() {
        User saved = userRepository.saveAndFlush(buildUser("bravo"));

        assertThat(userRepository.findByUserName(saved.getUserName()))
                .isPresent()
                .get()
                .extracting(User::getEmail)
                .isEqualTo(saved.getEmail());
    }

    @Test
    void findByFirstNameAndLastNameSupportsMultipleResults() {
        User first = buildUser("charlie");
        first.setFirstName("Alex");
        first.setLastName("Doe");
        userRepository.save(first);

        User second = buildUser("delta");
        second.setFirstName("Alex");
        second.setLastName("Doe");
        userRepository.saveAndFlush(second);

        List<User> results = userRepository.findByFirstNameAndLastName("Alex", "Doe");

        assertThat(results)
                .hasSize(2)
                .extracting(User::getUserName)
                .containsExactlyInAnyOrder(first.getUserName(), second.getUserName());
    }

    @Test
    void duplicateEmailIsRejected() {
        User first = buildUser("echo");
        first.setEmail("duplicate@example.com");
        userRepository.saveAndFlush(first);

        User second = buildUser("foxtrot");
        second.setEmail("duplicate@example.com");

        assertThatThrownBy(() -> userRepository.saveAndFlush(second))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void deletingUserCascadesToOwnedPlacesAndReviews() {
        User owner = buildUser("golf");
        owner.setDateOfBirth(LocalDate.of(1990, 1, 15));

        Activity activity = buildActivity(owner, "Hidden Falls");
        Review review = buildReview(activity, owner, Rating.FOUR_STAR);
        owner.getPlaces().add(activity);
        owner.getReviews().add(review);

        User persisted = userRepository.saveAndFlush(owner);
        UUID activityId = activity.getId();
        UUID reviewId = review.getId();

        userRepository.delete(persisted);
        userRepository.flush();
        entityManager.clear();

        assertThat(placeRepository.findById(activityId)).isEmpty();
        assertThat(reviewRepository.findById(reviewId)).isEmpty();
    }

    private User buildUser(String suffix) {
        User user = new User();
        user.setUserName("user-" + suffix);
        user.setFirstName("First" + suffix);
        user.setLastName("Last" + suffix);
        user.setEmail("user-" + suffix + "@example.com");
        user.setPasswordHash("hash-" + suffix);
        user.setCountryCode(CountryCode.NL);
        user.setLanguage(Language.EN);
        return user;
    }

    private Activity buildActivity(User owner, String name) {
        Activity activity = new Activity();
        activity.setName(name);
        activity.setOwner(owner);
        activity.setLocation(point(4.0, 50.0));
        activity.setColorCode("#00AAFF");
        activity.setHost("CampSo");
        activity.setTargetAudience(TargetAudience.FAMILIES);
        activity.setTotalSpaces(25);
        return activity;
    }

    private Review buildReview(Activity place, User owner, Rating rating) {
        Review review = new Review();
        review.setPlace(place);
        review.setOwner(owner);
        review.setRating(rating);
        review.setInfo("Great experience");
        place.addReview(review);
        return review;
    }
}
