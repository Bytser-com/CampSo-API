package com.bytser.campso.api.activities;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityRepository extends JpaRepository<Activity, UUID> {

    List<Activity> findByName(String name);

    List<Activity> findByOwnerId(UUID ownerId);

    List<Activity> findByHost(String host);
}
