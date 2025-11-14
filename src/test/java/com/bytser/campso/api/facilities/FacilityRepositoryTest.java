package com.bytser.campso.api.facilities;

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
class FacilityRepositoryTest {

    @Autowired
    private FacilityRepository facilityRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("findByHostPlaceId should return every facility attached to the host place")
    void shouldFindFacilitiesByHostPlaceId() {
        User owner = persistUser("900");
        Camping host = persistCamping(owner, "Central Park", 80.0);
        Camping otherHost = persistCamping(owner, "North Base", 81.0);
        Facility restroom = persistFacility(owner, host, "Restroom", FacilityType.RESTROOM, 80.0);
        Facility shower = persistFacility(owner, host, "Shower", FacilityType.SHOWER, 80.1);
        persistFacility(owner, otherHost, "Parking", FacilityType.PARKING, 81.0);

        entityManager.flush();
        entityManager.clear();

        List<Facility> result = facilityRepository.findByHostPlaceId(host.getId());

        assertThat(result)
            .extracting(Facility::getId)
            .containsExactlyInAnyOrder(restroom.getId(), shower.getId());
    }

    @Test
    @DisplayName("findByHostPlaceIdAndFacilityType should return facilities of that type for the host")
    void shouldFindFacilitiesByHostAndType() {
        User owner = persistUser("910");
        Camping host = persistCamping(owner, "Beach Front", 82.0);
        persistFacility(owner, host, "Wifi", FacilityType.WIFI, 82.0);
        Facility firstPlayground = persistFacility(owner, host, "Playground", FacilityType.PLAYGROUND, 82.1);
        Facility secondPlayground = persistFacility(owner, host, "Second Playground", FacilityType.PLAYGROUND, 82.2);

        entityManager.flush();
        entityManager.clear();

        List<Facility> result = facilityRepository.findByHostPlaceIdAndFacilityType(host.getId(), FacilityType.PLAYGROUND);

        assertThat(result)
            .extracting(Facility::getId)
            .containsExactlyInAnyOrder(firstPlayground.getId(), secondPlayground.getId());
        assertThat(result)
            .allMatch(facility -> facility.getFacilityType() == FacilityType.PLAYGROUND);
    }

    @Test
    @DisplayName("findByHostPlaceId should return an empty list when the host has no facilities")
    void shouldReturnEmptyFacilitiesWhenHostUnknown() {
        persistFacility(persistUser("920"), persistCamping(persistUser("921"), "Hidden", 83.0), "Trash", FacilityType.TRASH_DISPOSAL, 83.0);

        entityManager.flush();
        entityManager.clear();

        List<Facility> result = facilityRepository.findByHostPlaceId(UUID.randomUUID());

        assertThat(result).isEmpty();
    }

    private Camping persistCamping(User owner, String name, double longitude) {
        Camping camping = TestDataFactory.newCamping();
        camping.setName(name);
        camping.setOwner(owner);
        camping.setColorCode("#abcdef");
        camping.setLocation(TestDataFactory.createPoint(longitude, 15.0));
        camping.setInfo("Info for " + name);
        camping.setTargetAudience(TargetAudience.FAMILIES);
        camping.setTotalSpaces(45);
        return entityManager.persist(camping);
    }

    private Facility persistFacility(User owner, Camping host, String name, FacilityType type, double longitude) {
        Facility facility = TestDataFactory.newFacility();
        facility.setName(name);
        facility.setOwner(owner);
        facility.setColorCode("#445566");
        facility.setLocation(TestDataFactory.createPoint(longitude, 15.5));
        facility.setInfo("Facility info " + name);
        facility.setFacilityType(type);
        facility.setHostPlace(host);
        return entityManager.persist(facility);
    }

    private User persistUser(String identifier) {
        return entityManager.persist(TestDataFactory.createUser(identifier));
    }
}
