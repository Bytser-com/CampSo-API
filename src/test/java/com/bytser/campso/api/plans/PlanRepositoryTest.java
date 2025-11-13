package com.bytser.campso.api.plans;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.bytser.campso.api.RepositoryTestSupport;
import com.bytser.campso.api.campings.Camping;
import com.bytser.campso.api.campings.CampingRepository;
import com.bytser.campso.api.campings.TargetAudience;
import com.bytser.campso.api.users.CountryCode;
import com.bytser.campso.api.users.Language;
import com.bytser.campso.api.users.User;
import com.bytser.campso.api.users.UserRepository;

@DataJpaTest
class PlanRepositoryTest extends RepositoryTestSupport {

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private CampingRepository campingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void findByCampingIdReturnsAllPlansForCamping() {
        Camping camping = persistCampingWithPlans("camping-plan-1", "Summit Base");

        List<Plan> plans = planRepository.findByCampingId(camping.getId());

        assertThat(plans)
                .hasSize(2)
                .extracting(Plan::getName)
                .containsExactlyInAnyOrder("Tent Pitch", "Cabin Stay");
    }

    @Test
    void findByCampingIdAndNameFiltersPrecisely() {
        Camping camping = persistCampingWithPlans("camping-plan-2", "River Edge");

        List<Plan> plans = planRepository.findByCampingIdAndName(camping.getId(), "Cabin Stay");

        assertThat(plans)
                .hasSize(1)
                .first()
                .extracting(Plan::getMaxGuests)
                .isEqualTo(6);
    }

    @Test
    void removingPlanThroughCampingOrphanRemovesEntity() {
        Camping camping = persistCampingWithPlans("camping-plan-3", "Forest Edge");
        Plan plan = camping.getPlans().getFirst();
        UUID planId = plan.getId();

        camping.removePlan(plan);
        campingRepository.saveAndFlush(camping);
        entityManager.flush();
        entityManager.clear();

        assertThat(planRepository.findById(planId)).isEmpty();
    }

    private Camping persistCampingWithPlans(String suffix, String name) {
        User owner = persistUser(suffix);
        Camping camping = new Camping();
        camping.setName(name);
        camping.setOwner(owner);
        camping.setLocation(point(5.2, 50.5));
        camping.setColorCode("#55AAFF");
        camping.setTargetAudience(TargetAudience.MIXED);
        camping.setTotalSpaces(80);

        Plan tentPitch = buildPlan("Tent Pitch", 25.0, 4, true, true);
        Plan cabinStay = buildPlan("Cabin Stay", 110.0, 6, true, false);
        camping.addPlan(tentPitch);
        camping.addPlan(cabinStay);

        return campingRepository.saveAndFlush(camping);
    }

    private Plan buildPlan(String name, double price, int maxGuests, boolean available, boolean petsAllowed) {
        Plan plan = new Plan();
        plan.setName(name);
        plan.setDescription(name + " description");
        plan.setPricePerNight(price);
        plan.setMaxGuests(maxGuests);
        plan.setAvailable(available);
        plan.setPetsAllowed(petsAllowed);
        return plan;
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
