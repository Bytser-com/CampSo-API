package com.bytser.campso.api.campings;

import com.bytser.campso.api.facilities.Facility;
import com.bytser.campso.api.plans.Plan;
import com.bytser.campso.api.support.TestDataFactory;
import com.bytser.campso.api.users.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Geometry;

import static org.assertj.core.api.Assertions.assertThat;

class CampingTest {

    private Camping camping;
    private User owner;
    private Geometry location;

    @BeforeEach
    void setUp() {
        owner = TestDataFactory.createUser("CampingOwner");
        location = TestDataFactory.createPoint(7.0, 50.0);
        camping = new TestCamping();
        camping.setName("Forest Retreat");
        camping.setOwner(owner);
        camping.setLocation(location);
        camping.setColorCode("#00AA00");
        camping.setTargetAudience(TargetAudience.FAMILY);
        camping.setTotalSpaces(25);
    }

    @Test
    @DisplayName("addPlan establishes a bidirectional camping-plan association")
    void addPlanShouldLinkBothSides() {
        Plan plan = new TestPlan();
        plan.setName("Weekend Escape");
        plan.setDescription("Two nights");
        plan.setPricePerNight(89.99);
        plan.setMaxGuests(4);
        plan.setAvailable(true);
        plan.setPetsAllowed(false);

        camping.addPlan(plan);

        assertThat(camping.getPlans()).containsExactly(plan);
        assertThat(plan.getCamping()).isEqualTo(camping);
    }

    @Test
    @DisplayName("removePlan clears the camping-plan association")
    void removePlanShouldUnlinkBothSides() {
        Plan plan = new TestPlan();
        plan.setName("Weekend Escape");
        plan.setDescription("Two nights");
        plan.setPricePerNight(89.99);
        plan.setMaxGuests(4);
        plan.setAvailable(true);
        plan.setPetsAllowed(false);

        camping.addPlan(plan);
        camping.removePlan(plan);

        assertThat(camping.getPlans()).isEmpty();
        assertThat(plan.getCamping()).isNull();
    }

    @Test
    @DisplayName("addPlan ignores null plans")
    void addPlanShouldIgnoreNull() {
        camping.addPlan(null);

        assertThat(camping.getPlans()).isEmpty();
    }

    @Test
    @DisplayName("removePlan ignores null inputs")
    void removePlanShouldIgnoreNull() {
        camping.removePlan(null);

        assertThat(camping.getPlans()).isEmpty();
    }

    @Test
    @DisplayName("addFacility links the facility to the camping bidirectionally")
    void addFacilityShouldLinkBothSides() {
        Facility facility = new TestFacility();
        facility.setName("Shower Block");
        facility.setOwner(owner);
        facility.setLocation(location);
        facility.setColorCode("#CCCCCC");
        facility.setFacilityType(com.bytser.campso.api.facilities.FacilityType.SANITATION);

        camping.addFacility(facility);

        assertThat(camping.getFacilities()).containsExactly(facility);
        assertThat(facility.getHostPlace()).isEqualTo(camping);
    }

    @Test
    @DisplayName("removeFacility severs the bidirectional facility link")
    void removeFacilityShouldUnlinkBothSides() {
        Facility facility = new TestFacility();
        facility.setName("Shower Block");
        facility.setOwner(owner);
        facility.setLocation(location);
        facility.setColorCode("#CCCCCC");
        facility.setFacilityType(com.bytser.campso.api.facilities.FacilityType.SANITATION);

        camping.addFacility(facility);
        camping.removeFacility(facility);

        assertThat(camping.getFacilities()).isEmpty();
        assertThat(facility.getHostPlace()).isNull();
    }

    @Test
    @DisplayName("addFacility ignores null facilities")
    void addFacilityShouldIgnoreNull() {
        camping.addFacility(null);

        assertThat(camping.getFacilities()).isEmpty();
    }

    @Test
    @DisplayName("removeFacility ignores null inputs")
    void removeFacilityShouldIgnoreNull() {
        camping.removeFacility(null);

        assertThat(camping.getFacilities()).isEmpty();
    }

    @Test
    @DisplayName("toString includes name, audience, and capacity details")
    void toStringShouldContainKeyFields() {
        String asString = camping.toString();

        assertThat(asString)
                .contains("Forest Retreat")
                .contains("FAMILY")
                .contains("25");
    }

    private static class TestCamping extends Camping {
    }

    private static class TestPlan extends Plan {
    }

    private static class TestFacility extends Facility {
    }
}
