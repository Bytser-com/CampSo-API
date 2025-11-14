package com.bytser.campso.api.plans;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanRepository extends JpaRepository<Plan, UUID> {

    List<Plan> findByCampingId(UUID campingId);

    List<Plan> findByCampingIdAndName(UUID campingId, String name);
}
