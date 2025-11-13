package com.bytser.campso.api.places;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place, UUID> {

    Optional<Place> findByName(String name);

    List<Place> findByOwnerId(UUID ownerId);
}
