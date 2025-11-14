package com.bytser.campso.api.activities;

import static org.assertj.core.api.Assertions.assertThat;

import com.bytser.campso.api.support.TestDataFactory;
import com.bytser.campso.api.users.User;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest(properties = "spring.jpa.hibernate.ddl-auto=create-drop")
class ActivityRepositoryTest {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("findByName should return all activities with the requested name")
    void shouldFindActivitiesByName() {
        User owner = persistUser("700");
        Activity first = persistActivity(owner, "Kayak Adventure", 60.0, "Host A");
        Activity second = persistActivity(owner, "Kayak Adventure", 61.0, "Host B");
        persistActivity(owner, "Hiking Trail", 62.0, "Host C");

        entityManager.flush();
        entityManager.clear();

        List<Activity> result = activityRepository.findByName("Kayak Adventure");

        assertThat(result)
            .extracting(Activity::getId)
            .containsExactlyInAnyOrder(first.getId(), second.getId());
    }

    @Test
    @DisplayName("findByOwnerId should return every activity that belongs to the owner")
    void shouldFindActivitiesByOwnerId() {
        User owner = persistUser("710");
        User other = persistUser("711");
        Activity first = persistActivity(owner, "Mountain Biking", 63.0, "Host D");
        Activity second = persistActivity(owner, "Cliff Diving", 64.0, "Host E");
        persistActivity(other, "City Tour", 65.0, "Host F");

        entityManager.flush();
        entityManager.clear();

        List<Activity> result = activityRepository.findByOwnerId(owner.getId());

        assertThat(result)
            .extracting(Activity::getId)
            .containsExactlyInAnyOrder(first.getId(), second.getId());
    }

    @Test
    @DisplayName("findByHost should return activities hosted by the provided host")
    void shouldFindActivitiesByHost() {
        User owner = persistUser("720");
        persistActivity(owner, "Sunrise Yoga", 66.0, "Host Yoga");
        persistActivity(owner, "Evening Yoga", 67.0, "Host Yoga");
        persistActivity(owner, "Cooking Class", 68.0, "Chef Anna");

        entityManager.flush();
        entityManager.clear();

        List<Activity> result = activityRepository.findByHost("Host Yoga");

        assertThat(result)
            .extracting(Activity::getName)
            .containsExactlyInAnyOrder("Sunrise Yoga", "Evening Yoga");
        assertThat(result).allMatch(activity -> "Host Yoga".equals(activity.getHost()));
        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("findByHost should return an empty list when the host has no activities")
    void shouldReturnEmptyListWhenHostUnknown() {
        persistActivity(persistUser("730"), "Rock Climbing", 69.0, "Host G");

        entityManager.flush();
        entityManager.clear();

        List<Activity> result = activityRepository.findByHost("missing");

        assertThat(result).isEmpty();
    }

    private Activity persistActivity(User owner, String name, double longitude, String host) {
        Activity activity = new Activity();
        activity.setName(name);
        activity.setOwner(owner);
        activity.setColorCode("#112233");
        activity.setLocation(TestDataFactory.createPoint(longitude, 13.0));
        activity.setInfo("Info for " + name);
        activity.setTargetAudience(TargetAudience.MIXED);
        activity.setTotalSpaces(25);
        activity.setHost(host);
        return entityManager.persist(activity);
    }

    private User persistUser(String identifier) {
        return entityManager.persist(TestDataFactory.createUser(identifier));
    }
}
