package com.bytser.campso.api.places;

import static org.assertj.core.api.Assertions.assertThat;

import com.bytser.campso.api.activities.Activity;
import com.bytser.campso.api.activities.TargetAudience;
import com.bytser.campso.api.campings.Camping;
import com.bytser.campso.api.support.TestDataFactory;
import com.bytser.campso.api.users.User;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest(properties = "spring.jpa.hibernate.ddl-auto=create-drop")
class PlaceRepositoryTest {

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("findByName should return all places with the same name regardless of type")
    void shouldFindPlacesByName() {
        User owner = persistUser("500");
        Activity activity = persistActivity(owner, "Trail Camp", 40.0);
        Camping camping = persistCamping(owner, "Trail Camp", 41.0);
        persistActivity(owner, "Lakeside", 42.0);

        entityManager.flush();
        entityManager.clear();

        List<Place> result = placeRepository.findByName("Trail Camp");

        assertThat(result)
            .extracting(Place::getId)
            .containsExactlyInAnyOrder(activity.getId(), camping.getId());
    }

    @Test
    @DisplayName("findByOwnerId should return every place that belongs to the owner")
    void shouldFindPlacesByOwnerId() {
        User firstOwner = persistUser("510");
        User secondOwner = persistUser("511");
        Activity first = persistActivity(firstOwner, "Woodland", 45.0);
        Camping second = persistCamping(firstOwner, "Forest Base", 46.0);
        persistActivity(secondOwner, "Downtown", 47.0);

        entityManager.flush();
        entityManager.clear();

        List<Place> result = placeRepository.findByOwnerId(firstOwner.getId());

        assertThat(result)
            .extracting(Place::getId)
            .containsExactlyInAnyOrder(first.getId(), second.getId());
    }

    private Activity persistActivity(User owner, String name, double longitude) {
        Activity activity = new Activity();
        activity.setName(name);
        activity.setOwner(owner);
        activity.setColorCode("#123456");
        activity.setLocation(TestDataFactory.createPoint(longitude, 10.0));
        activity.setInfo("Info " + name);
        activity.setHost("Host " + name);
        activity.setTargetAudience(TargetAudience.FAMILIES);
        activity.setTotalSpaces(20);
        return entityManager.persist(activity);
    }

    private Camping persistCamping(User owner, String name, double longitude) {
        Camping camping = new Camping();
        camping.setName(name);
        camping.setOwner(owner);
        camping.setColorCode("#654321");
        camping.setLocation(TestDataFactory.createPoint(longitude, 11.0));
        camping.setInfo("Info " + name);
        camping.setTargetAudience(com.bytser.campso.api.campings.TargetAudience.MIXED);
        camping.setTotalSpaces(30);
        return entityManager.persist(camping);
    }

    private User persistUser(String identifier) {
        return entityManager.persist(TestDataFactory.createUser(identifier));
    }
}
