package com.bytser.campso.api.facilities;

import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.locationtech.jts.geom.*;

import com.bytser.campso.api.places.Place;

import jakarta.persistence.*;

@Entity
@Table(name = "facilities")
public class Facility extends Place {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique=false, updatable = true)
    private FacilityType facilityType;

    // Getters and Setters
    public FacilityType getFacilityType() {
        return facilityType;
    }
    public void setFacilityType(FacilityType facilityType) {
        this.facilityType = facilityType;
    }
}
