package com.bytser.campso.api.facilities;

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
class FacilityRepositoryTest extends RepositoryTestSupport {

    @Autowired
    private FacilityRepository facilityRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void findByHostPlaceIdReturnsFacilitiesForThatPlace() {
        Activity activity = persistActivity("facility-host-1", "Adventure Hub");
        Facility restroom = buildFacility(activity, FacilityType.RESTROOM, "#4455AA", 4.7, 50.7);
        Facility wifi = buildFacility(activity, FacilityType.WIFI, "#112233", 4.71, 50.71);
        facilityRepository.save(restroom);
        facilityRepository.saveAndFlush(wifi);

        List<Facility> results = facilityRepository.findByHostPlaceId(activity.getId());

        assertThat(results)
                .hasSize(2)
                .extracting(Facility::getFacilityType)
                .containsExactlyInAnyOrder(FacilityType.RESTROOM, FacilityType.WIFI);
    }

    @Test
    void findByHostPlaceIdAndFacilityTypeIsPrecise() {
        Activity activity = persistActivity("facility-host-2", "Relax Corner");
        Facility parking = buildFacility(activity, FacilityType.PARKING, "#2266AA", 4.9, 50.9);
        Facility restroom = buildFacility(activity, FacilityType.RESTROOM, "#BB3344", 4.91, 50.91);
        facilityRepository.save(parking);
        facilityRepository.saveAndFlush(restroom);

        List<Facility> results = facilityRepository.findByHostPlaceIdAndFacilityType(activity.getId(), FacilityType.RESTROOM);

        assertThat(results)
                .hasSize(1)
                .first()
                .extracting(Facility::getName)
                .isEqualTo(restroom.getName());
    }

    @Test
    void removingFacilityThroughActivityCleansUpOrphans() {
        Activity activity = persistActivity("facility-host-3", "Skill Park");
        Facility facility = buildFacility(activity, FacilityType.FIRE_PIT, "#EE5500", 5.1, 51.1);
        facilityRepository.saveAndFlush(facility);
        UUID facilityId = facility.getId();

        activity.removeFacility(facility);
        activityRepository.saveAndFlush(activity);
        entityManager.flush();
        entityManager.clear();

        assertThat(facilityRepository.findById(facilityId)).isEmpty();
    }

    private Activity persistActivity(String suffix, String name) {
        User owner = persistUser(suffix);
        Activity activity = new Activity();
        activity.setName(name);
        activity.setOwner(owner);
        activity.setLocation(point(5.4, 50.6));
        activity.setColorCode("#AA5522");
        activity.setHost("Host " + suffix);
        activity.setTargetAudience(TargetAudience.MIXED);
        activity.setTotalSpaces(55);
        return activityRepository.saveAndFlush(activity);
    }

    private Facility buildFacility(Activity host, FacilityType type, String color, double longitude, double latitude) {
        Facility facility = new Facility();
        facility.setName(type.name() + " Facility");
        facility.setOwner(host.getOwner());
        facility.setLocation(point(longitude, latitude));
        facility.setColorCode(color);
        facility.setFacilityType(type);
        host.addFacility(facility);
        return facility;
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
