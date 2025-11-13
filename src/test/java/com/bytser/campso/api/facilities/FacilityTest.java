package com.bytser.campso.api.facilities;

import com.bytser.campso.api.activities.Activity;
import com.bytser.campso.api.support.TestDataFactory;
import com.bytser.campso.api.users.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Geometry;

import static org.assertj.core.api.Assertions.assertThat;

class FacilityTest {

    private Facility facility;
    private User owner;
    private Geometry location;

    @BeforeEach
    @SuppressWarnings("unused") // compiler gives warning that function is never used, but function is used by Spring Boot annotations (@BeforeEach)
    void setUp() {
        owner = TestDataFactory.createUser("FacilityOwner");
        location = TestDataFactory.createPoint(6.0, 53.0);
        facility = new TestFacility();
        facility.setName("Community Kitchen");
        facility.setOwner(owner);
        facility.setLocation(location);
        facility.setColorCode("#FFD700");
        facility.setInfo("Shared cooking space");
        facility.setFacilityType(FacilityType.ENTRANCE);
    }

    @Test
    @DisplayName("setHostPlace assigns an owning place to the facility")
    void setHostPlaceShouldLinkFacilityToPlace() {
        Activity host = new TestActivity();
        host.setName("Cooking Class");
        host.setOwner(owner);
        host.setLocation(location);
        host.setColorCode("#990000");

        facility.setHostPlace(host);

        assertThat(facility.getHostPlace()).isEqualTo(host);
    }

    @Test
    @DisplayName("setHostPlace with null removes the existing association")
    void setHostPlaceNullShouldUnlink() {
        Activity host = new TestActivity();
        host.setName("Cooking Class");
        host.setOwner(owner);
        host.setLocation(location);
        host.setColorCode("#990000");

        facility.setHostPlace(host);
        facility.setHostPlace(null);

        assertThat(facility.getHostPlace()).isNull();
    }

    @Test
    @DisplayName("setHostPlace replaces an existing host association")
    void setHostPlaceShouldReplaceExistingHost() {
        Activity firstHost = new TestActivity();
        firstHost.setName("Cooking Class");
        firstHost.setOwner(owner);
        firstHost.setLocation(location);
        firstHost.setColorCode("#990000");

        Activity newHost = new TestActivity();
        newHost.setName("Baking Workshop");
        newHost.setOwner(owner);
        newHost.setLocation(location);
        newHost.setColorCode("#550000");

        facility.setHostPlace(firstHost);
        facility.setHostPlace(newHost);

        assertThat(facility.getHostPlace()).isEqualTo(newHost);
    }

    @Test
    @DisplayName("setFacilityType updates the stored type")
    void setFacilityTypeShouldUpdateValue() {
        facility.setFacilityType(FacilityType.PARKING);

        assertThat(facility.getFacilityType()).isEqualTo(FacilityType.PARKING);
    }

    @Test
    @DisplayName("toString includes the facility name and type")
    void toStringShouldContainKeyFields() {
        String asString = facility.toString();

        assertThat(asString)
                .contains("Community Kitchen")
                .contains("ENTRANCE");
    }

    private static class TestFacility extends Facility {
    }

    private static class TestActivity extends Activity {
    }
}
