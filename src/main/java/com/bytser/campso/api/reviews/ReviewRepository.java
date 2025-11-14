package com.bytser.campso.api.reviews;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, UUID> {

    List<Review> findByPlaceId(UUID placeId);

    List<Review> findByOwnerId(UUID ownerId);
}
