package com.bytser.campso.api.activities;

import com.bytser.campso.api.facilities.Facility;
import com.bytser.campso.api.support.TestDataFactory;
import com.bytser.campso.api.users.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Geometry;

import static org.assertj.core.api.Assertions.assertThat;

class ActivityTest {

    private Activity activity;
    private User owner;
    private Geometry location;

    @BeforeEach
    @SuppressWarnings("unused") // compiler gives warning that function is never used, but function is used by Spring Boot annotations (@BeforeEach)
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
        activity.setTargetAudience(TargetAudience.FAMILIES);
        activity.setTotalSpaces(12);
    }

    @Test
    @DisplayName("addFacility links the facility with the activity bidirectionally")
    void addFacilityShouldLinkBothSides() {
        Facility facility = new TestFacility();
        facility.setName("Boat House");
        facility.setOwner(owner);
        facility.setLocation(location);
        facility.setColorCode("#123456");
        facility.setFacilityType(com.bytser.campso.api.facilities.FacilityType.OTHER);

        activity.addFacility(facility);

        assertThat(activity.getFacilities()).containsExactly(facility);
        assertThat(facility.getHostPlace()).isEqualTo(activity);
    }

    @Test
    @DisplayName("addFacility ignores null input")
    void addFacilityShouldIgnoreNull() {
        activity.addFacility(null);
        assertThat(activity.getFacilities()).isEmpty();
    }

    @Test
    @DisplayName("removeFacility clears the association on both sides")
    void removeFacilityShouldUnlinkBothSides() {
        Facility facility = new TestFacility();
        facility.setName("Boat House");
        facility.setOwner(owner);
        facility.setLocation(location);
        facility.setColorCode("#123456");
        facility.setFacilityType(com.bytser.campso.api.facilities.FacilityType.OTHER);

        activity.addFacility(facility);
        activity.removeFacility(facility);

        assertThat(activity.getFacilities()).isEmpty();
        assertThat(facility.getHostPlace()).isNull();
    }

    @Test
    @DisplayName("removeFacility ignores null input")
    void removeFacilityShouldIgnoreNull() {
        activity.removeFacility(null);
        assertThat(activity.getFacilities()).isEmpty();
    }

    @Test
    @DisplayName("addSchedule links the schedule and activity bidirectionally")
    void addScheduleShouldLinkBothSides() {
        ActivitySchedule schedule = new ActivitySchedule();

        activity.addSchedule(schedule);

        assertThat(activity.getSchedules()).containsExactly(schedule);
        assertThat(schedule.getActivity()).isEqualTo(activity);
    }

    @Test
    @DisplayName("addSchedule ignores null input")
    void addScheduleShouldIgnoreNull() {
        activity.addSchedule(null);
        assertThat(activity.getSchedules()).isEmpty();
    }

    @Test
    @DisplayName("removeSchedule removes both sides of the association")
    void removeScheduleShouldUnlinkBothSides() {
        ActivitySchedule schedule = new ActivitySchedule();
        activity.addSchedule(schedule);

        activity.removeSchedule(schedule);

        assertThat(activity.getSchedules()).isEmpty();
        assertThat(schedule.getActivity()).isNull();
    }

    @Test
    @DisplayName("removeSchedule ignores null input")
    void removeScheduleShouldIgnoreNull() {
        activity.removeSchedule(null);
        assertThat(activity.getSchedules()).isEmpty();
    }

    @Test
    @DisplayName("toString includes name, host, target audience, and capacity")
    void toStringShouldContainKeyFields() {
        String asString = activity.toString();

        assertThat(asString)
                .contains("Kayaking Adventure")
                .contains("River Guides")
                .contains("FAMILIES")
                .contains("12");
    }

    private static class TestActivity extends Activity {
    }

    private static class TestFacility extends Facility {
    }
}
