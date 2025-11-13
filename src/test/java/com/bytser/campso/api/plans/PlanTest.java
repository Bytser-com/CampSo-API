package com.bytser.campso.api.plans;

import com.bytser.campso.api.campings.Camping;
import com.bytser.campso.api.support.TestDataFactory;
import com.bytser.campso.api.users.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PlanTest {

    private Plan plan;

    @BeforeEach
    @SuppressWarnings("unused") // compiler gives warning that function is never used, but function is used by Spring Boot annotations (@BeforeEach)
    void setUp() {
        plan = new TestPlan();
        plan.setName("Family Package");
        plan.setDescription("Includes breakfast");
        plan.setPricePerNight(120.5);
        plan.setMaxGuests(6);
        plan.setAvailable(true);
        plan.setPetsAllowed(true);
    }

    @Test
    @DisplayName("setCamping associates the plan with a camping")
    void setCampingShouldLinkPlanToCamping() {
        User owner = TestDataFactory.createUser("PlanOwner");
        Camping camping = new TestCamping();
        camping.setName("Mountain Base");
        camping.setOwner(owner);
        camping.setLocation(TestDataFactory.createPoint(3.0, 45.0));
        camping.setColorCode("#445566");
        camping.setTargetAudience(com.bytser.campso.api.campings.TargetAudience.SENIORS);
        camping.setTotalSpaces(15);

        plan.setCamping(camping);

        assertThat(plan.getCamping()).isEqualTo(camping);
    }

    @Test
    @DisplayName("setCamping allows detaching the plan from a camping")
    void setCampingShouldAllowDetaching() {
        User owner = TestDataFactory.createUser("PlanOwner");
        Camping camping = new TestCamping();
        camping.setName("Mountain Base");
        camping.setOwner(owner);
        camping.setLocation(TestDataFactory.createPoint(3.0, 45.0));
        camping.setColorCode("#445566");
        camping.setTargetAudience(com.bytser.campso.api.campings.TargetAudience.SENIORS);
        camping.setTotalSpaces(15);
        plan.setCamping(camping);

        plan.setCamping(null);

        assertThat(plan.getCamping()).isNull();
    }

    @Test
    @DisplayName("toString lists plan configuration fields")
    void toStringShouldContainKeyFields() {
        String asString = plan.toString();

        assertThat(asString)
                .contains("name='Family Package'")
                .contains("pricePerNight=120.5")
                .contains("maxGuests=6")
                .contains("available=true")
                .contains("petsAllowed=true");
    }

    private static class TestPlan extends Plan {
    }

    private static class TestCamping extends Camping {
    }
}
