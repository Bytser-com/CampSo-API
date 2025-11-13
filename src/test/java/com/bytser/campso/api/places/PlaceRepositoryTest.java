package com.bytser.campso.api.places;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.bytser.campso.api.RepositoryTestSupport;
import com.bytser.campso.api.activities.Activity;
import com.bytser.campso.api.activities.TargetAudience;
import com.bytser.campso.api.campings.Camping;
import com.bytser.campso.api.places.Place;
import com.bytser.campso.api.users.CountryCode;
import com.bytser.campso.api.users.Language;
import com.bytser.campso.api.users.User;
import com.bytser.campso.api.users.UserRepository;

@DataJpaTest
class PlaceRepositoryTest extends RepositoryTestSupport {

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByNameReturnsAllMatchingSubtypes() {
        User owner = persistUser("place-owner");
        Activity activity = buildActivity(owner, "Sunrise Haven");
        Camping camping = buildCamping(owner, "Sunrise Haven");

        placeRepository.save(activity);
        placeRepository.saveAndFlush(camping);

        List<Place> results = placeRepository.findByName("Sunrise Haven");

        assertThat(results)
                .hasSize(2)
                .extracting(Place::getClass)
                .containsExactlyInAnyOrder(Activity.class, Camping.class);
    }

    @Test
    void findByOwnerIdReturnsOnlyMatchingPlaces() {
        User owner = persistUser("primary-owner");
        Activity activity = buildActivity(owner, "Forest Yoga");
        placeRepository.save(activity);

        User otherOwner = persistUser("secondary-owner");
        Camping camping = buildCamping(otherOwner, "Mountain Base");
        placeRepository.saveAndFlush(camping);

        List<Place> results = placeRepository.findByOwnerId(owner.getId());

        assertThat(results)
                .hasSize(1)
                .first()
                .extracting(Place::getName)
                .isEqualTo("Forest Yoga");
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

    private Activity buildActivity(User owner, String name) {
        Activity activity = new Activity();
        activity.setName(name);
        activity.setOwner(owner);
        activity.setLocation(point(5.0, 51.0));
        activity.setColorCode("#00AA11");
        activity.setHost("CampSo");
        activity.setTargetAudience(TargetAudience.YOUNG_ADULTS);
        activity.setTotalSpaces(30);
        return activity;
    }

    private Camping buildCamping(User owner, String name) {
        Camping camping = new Camping();
        camping.setName(name);
        camping.setOwner(owner);
        camping.setLocation(point(5.5, 51.5));
        camping.setColorCode("#FF6600");
        camping.setTargetAudience(com.bytser.campso.api.campings.TargetAudience.FAMILIES);
        camping.setTotalSpaces(100);
        return camping;
    }
}
