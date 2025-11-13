package com.bytser.campso.api.facilities;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FacilityRepository extends JpaRepository<Facility, UUID> {

    List<Facility> findByHostPlaceId(UUID hostPlaceId);

    List<Facility> findByFacilityType(FacilityType facilityType);
}
