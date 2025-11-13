package com.bytser.campso.api.campings;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CampingRepository extends JpaRepository<Camping, UUID> {

    Optional<Camping> findByName(String name);

    List<Camping> findByOwnerId(UUID ownerId);
}
