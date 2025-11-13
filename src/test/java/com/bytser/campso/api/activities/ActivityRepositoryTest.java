package com.bytser.campso.api.activities;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.bytser.campso.api.RepositoryTestSupport;
import com.bytser.campso.api.users.CountryCode;
import com.bytser.campso.api.users.Language;
import com.bytser.campso.api.users.User;
import com.bytser.campso.api.users.UserRepository;

@DataJpaTest
class ActivityRepositoryTest extends RepositoryTestSupport {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByNameReturnsExactMatches() {
        User owner = persistUser("activity-owner");
        Activity yoga = buildActivity(owner, "Forest Yoga", "Luna");
        Activity hike = buildActivity(owner, "Sunset Hike", "Milo");
        activityRepository.save(yoga);
        activityRepository.saveAndFlush(hike);

        List<Activity> results = activityRepository.findByName("Forest Yoga");

        assertThat(results)
                .hasSize(1)
                .first()
                .extracting(Activity::getHost)
                .isEqualTo("Luna");
    }

    @Test
    void findByOwnerIdReturnsAllOwnedActivities() {
        User owner = persistUser("activity-owner-2");
        activityRepository.save(buildActivity(owner, "Kayak Adventure", "Kai"));
        activityRepository.save(buildActivity(owner, "Cliff Climbing", "Iris"));

        User otherOwner = persistUser("activity-owner-3");
        activityRepository.saveAndFlush(buildActivity(otherOwner, "City Walk", "Nova"));

        List<Activity> results = activityRepository.findByOwnerId(owner.getId());

        assertThat(results)
                .hasSize(2)
                .extracting(Activity::getName)
                .containsExactlyInAnyOrder("Kayak Adventure", "Cliff Climbing");
    }

    @Test
    void findByHostIsCaseSensitiveAndPrecise() {
        User owner = persistUser("activity-owner-4");
        Activity hostedByRita = buildActivity(owner, "Tea Workshop", "Rita");
        Activity hostedByLowercase = buildActivity(owner, "Coffee Workshop", "rita");
        activityRepository.save(hostedByRita);
        activityRepository.saveAndFlush(hostedByLowercase);

        List<Activity> results = activityRepository.findByHost("Rita");

        assertThat(results)
                .hasSize(1)
                .first()
                .extracting(Activity::getName)
                .isEqualTo("Tea Workshop");
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

    private Activity buildActivity(User owner, String name, String host) {
        Activity activity = new Activity();
        activity.setName(name);
        activity.setOwner(owner);
        activity.setLocation(point(4.3, 50.8));
        activity.setColorCode("#AA00FF");
        activity.setHost(host);
        activity.setTargetAudience(TargetAudience.MIXED);
        activity.setTotalSpaces(40);
        return activity;
    }
}
