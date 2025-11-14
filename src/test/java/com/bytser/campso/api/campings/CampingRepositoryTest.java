package com.bytser.campso.api.campings;

import static org.assertj.core.api.Assertions.assertThat;

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
class CampingRepositoryTest {

    @Autowired
    private CampingRepository campingRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("findByName should return all campings that share the requested name")
    void shouldFindCampingsByName() {
        User owner = persistUser("600");
        Camping first = persistCamping(owner, "Sunset Escape", 50.0);
        Camping second = persistCamping(owner, "Sunset Escape", 51.0);
        persistCamping(owner, "Mountain Base", 52.0);

        entityManager.flush();
        entityManager.clear();

        List<Camping> result = campingRepository.findByName("Sunset Escape");

        assertThat(result)
            .extracting(Camping::getId)
            .containsExactlyInAnyOrder(first.getId(), second.getId());
    }

    @Test
    @DisplayName("findByOwnerId should return every camping owned by the user")
    void shouldFindCampingsByOwnerId() {
        User owner = persistUser("610");
        User otherOwner = persistUser("611");
        Camping first = persistCamping(owner, "Riverfront", 53.0);
        Camping second = persistCamping(owner, "Forest Retreat", 54.0);
        persistCamping(otherOwner, "City Base", 55.0);

        entityManager.flush();
        entityManager.clear();

        List<Camping> result = campingRepository.findByOwnerId(owner.getId());

        assertThat(result)
            .extracting(Camping::getId)
            .containsExactlyInAnyOrder(first.getId(), second.getId());
    }

    @Test
    @DisplayName("findByOwnerId should return an empty list when no campings match")
    void shouldReturnEmptyListForUnknownOwner() {
        persistCamping(persistUser("620"), "Hidden Cove", 56.0);

        entityManager.flush();
        entityManager.clear();

        List<Camping> result = campingRepository.findByOwnerId(UUID.randomUUID());

        assertThat(result).isEmpty();
    }

    private Camping persistCamping(User owner, String name, double longitude) {
        Camping camping = TestDataFactory.newCamping();
        camping.setName(name);
        camping.setOwner(owner);
        camping.setColorCode("#abcdef");
        camping.setLocation(TestDataFactory.createPoint(longitude, 12.0));
        camping.setInfo("Info for " + name);
        camping.setTargetAudience(TargetAudience.FAMILIES);
        camping.setTotalSpaces(40);
        return entityManager.persist(camping);
    }

    private User persistUser(String identifier) {
        return entityManager.persist(TestDataFactory.createUser(identifier));
    }
}
