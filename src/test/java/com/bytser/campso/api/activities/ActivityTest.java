package com.bytser.campso.api.activities;

import com.bytser.campso.api.facilities.Facility;
import com.bytser.campso.api.support.TestDataFactory;
import com.bytser.campso.api.users.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Geometry;

import static org.assertj.core.api.Assertions.assertThat;

class ActivityTest {

    private Activity activity;
    private User owner;
    private Geometry location;

    @BeforeEach
    void setUp() {
        owner = TestDataFactory.createUser("ActivityOwner");
        location = TestDataFactory.createPoint(4.0, 52.0);
        activity = new TestActivity();
        activity.setName("Kayaking Adventure");
        activity.setOwner(owner);
        activity.setLocation(location);
        activity.setColorCode("#FF00FF");
        activity.setInfo("Enjoy a day on the water");
        activity.setHost("River Guides");
        activity.setTargetAudience(TargetAudience.FAMILY);
        activity.setTotalSpaces(12);
    }

    @Test
    void addFacilityShouldLinkBothSides() {
        Facility facility = new TestFacility();
        facility.setName("Boat House");
        facility.setOwner(owner);
        facility.setLocation(location);
        facility.setColorCode("#123456");
        facility.setFacilityType(com.bytser.campso.api.facilities.FacilityType.SPORTS);

        activity.addFacility(facility);

        assertThat(activity.getFacilities()).containsExactly(facility);
        assertThat(facility.getHostPlace()).isEqualTo(activity);
    }

    @Test
    void addFacilityShouldIgnoreNull() {
        activity.addFacility(null);
        assertThat(activity.getFacilities()).isEmpty();
    }

    @Test
    void removeFacilityShouldUnlinkBothSides() {
        Facility facility = new TestFacility();
        facility.setName("Boat House");
        facility.setOwner(owner);
        facility.setLocation(location);
        facility.setColorCode("#123456");
        facility.setFacilityType(com.bytser.campso.api.facilities.FacilityType.SPORTS);

        activity.addFacility(facility);
        activity.removeFacility(facility);

        assertThat(activity.getFacilities()).isEmpty();
        assertThat(facility.getHostPlace()).isNull();
    }

    @Test
    void removeFacilityShouldIgnoreNull() {
        activity.removeFacility(null);
        assertThat(activity.getFacilities()).isEmpty();
    }

    @Test
    void addScheduleShouldLinkBothSides() {
        ActivitySchedule schedule = new ActivitySchedule();

        activity.addSchedule(schedule);

        assertThat(activity.getSchedules()).containsExactly(schedule);
        assertThat(schedule.getActivity()).isEqualTo(activity);
    }

    @Test
    void addScheduleShouldIgnoreNull() {
        activity.addSchedule(null);
        assertThat(activity.getSchedules()).isEmpty();
    }

    @Test
    void removeScheduleShouldUnlinkBothSides() {
        ActivitySchedule schedule = new ActivitySchedule();
        activity.addSchedule(schedule);

        activity.removeSchedule(schedule);

        assertThat(activity.getSchedules()).isEmpty();
        assertThat(schedule.getActivity()).isNull();
    }

    @Test
    void removeScheduleShouldIgnoreNull() {
        activity.removeSchedule(null);
        assertThat(activity.getSchedules()).isEmpty();
    }

    @Test
    void toStringShouldContainKeyFields() {
        String asString = activity.toString();

        assertThat(asString)
                .contains("Kayaking Adventure")
                .contains("River Guides")
                .contains("FAMILY")
                .contains("12");
    }

    private static class TestActivity extends Activity {
    }

    private static class TestFacility extends Facility {
    }
}
