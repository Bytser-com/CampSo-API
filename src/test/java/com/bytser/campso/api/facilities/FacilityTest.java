package com.bytser.campso.api.facilities;

import com.bytser.campso.api.activities.Activity;
import com.bytser.campso.api.support.TestDataFactory;
import com.bytser.campso.api.users.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Geometry;

import static org.assertj.core.api.Assertions.assertThat;

class FacilityTest {

    private Facility facility;
    private User owner;
    private Geometry location;

    @BeforeEach
    void setUp() {
        owner = TestDataFactory.createUser("FacilityOwner");
        location = TestDataFactory.createPoint(6.0, 53.0);
        facility = new TestFacility();
        facility.setName("Community Kitchen");
        facility.setOwner(owner);
        facility.setLocation(location);
        facility.setColorCode("#FFD700");
        facility.setInfo("Shared cooking space");
        facility.setFacilityType(FacilityType.DINING);
    }

    @Test
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
    void toStringShouldContainKeyFields() {
        String asString = facility.toString();

        assertThat(asString)
                .contains("Community Kitchen")
                .contains("DINING");
    }

    private static class TestFacility extends Facility {
    }

    private static class TestActivity extends Activity {
    }
}
