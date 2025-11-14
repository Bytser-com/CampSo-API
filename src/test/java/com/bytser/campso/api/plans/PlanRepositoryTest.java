package com.bytser.campso.api.plans;

import static org.assertj.core.api.Assertions.assertThat;

import com.bytser.campso.api.campings.Camping;
import com.bytser.campso.api.campings.TargetAudience;
import com.bytser.campso.api.support.TestDataFactory;
import com.bytser.campso.api.users.User;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest(properties = "spring.jpa.hibernate.ddl-auto=create-drop")
class PlanRepositoryTest {

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("findByCampingId should return all plans that belong to the camping")
    void shouldFindPlansByCampingId() {
        User owner = persistUser("800");
        Camping camping = persistCamping(owner, "Lakeside", 70.0);
        Camping otherCamping = persistCamping(owner, "Mountain", 71.0);
        Plan first = persistPlan(camping, "Standard", 50.0);
        Plan second = persistPlan(camping, "Premium", 80.0);
        persistPlan(otherCamping, "Budget", 30.0);

        entityManager.flush();
        entityManager.clear();

        List<Plan> result = planRepository.findByCampingId(camping.getId());

        assertThat(result)
            .extracting(Plan::getId)
            .containsExactlyInAnyOrder(first.getId(), second.getId());
    }

    @Test
    @DisplayName("findByCampingIdAndName should return the single matching plan")
    void shouldFindPlansByCampingIdAndName() {
        User owner = persistUser("810");
        Camping camping = persistCamping(owner, "Forest", 72.0);
        Plan expected = persistPlan(camping, "Deluxe", 120.0);
        persistPlan(camping, "Economy", 40.0);

        entityManager.flush();
        entityManager.clear();

        List<Plan> result = planRepository.findByCampingIdAndName(camping.getId(), "Deluxe");

        assertThat(result)
            .singleElement()
            .satisfies(plan -> {
                assertThat(plan.getId()).isEqualTo(expected.getId());
                assertThat(plan.getName()).isEqualTo("Deluxe");
                assertThat(plan.getPricePerNight()).isEqualTo(120.0);
            });
    }

    @Test
    @DisplayName("findByCampingIdAndName should return empty when the name does not exist")
    void shouldReturnEmptyListWhenPlanNameMissing() {
        User owner = persistUser("820");
        Camping camping = persistCamping(owner, "River", 73.0);
        persistPlan(camping, "Explorer", 90.0);

        entityManager.flush();
        entityManager.clear();

        List<Plan> result = planRepository.findByCampingIdAndName(camping.getId(), "Unknown");

        assertThat(result).isEmpty();
    }

    private Camping persistCamping(User owner, String name, double longitude) {
        Camping camping = new Camping();
        camping.setName(name);
        camping.setOwner(owner);
        camping.setColorCode("#d4d4d4");
        camping.setLocation(TestDataFactory.createPoint(longitude, 14.0));
        camping.setInfo("Info for " + name);
        camping.setTargetAudience(TargetAudience.MIXED);
        camping.setTotalSpaces(60);
        return entityManager.persist(camping);
    }

    private Plan persistPlan(Camping camping, String name, double price) {
        Plan plan = new Plan();
        plan.setCamping(camping);
        plan.setName(name);
        plan.setDescription(name + " plan");
        plan.setPricePerNight(price);
        plan.setMaxGuests(4);
        plan.setAvailable(true);
        plan.setPetsAllowed(false);
        return entityManager.persist(plan);
    }

    private User persistUser(String identifier) {
        return entityManager.persist(TestDataFactory.createUser(identifier));
    }
}
